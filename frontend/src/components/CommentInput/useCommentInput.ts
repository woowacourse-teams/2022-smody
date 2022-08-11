import { queryKeys } from 'apis/constants';
import { usePostComment } from 'apis/feedApi';
import { useState, useRef, ChangeEvent } from 'react';
import { useQueryClient } from 'react-query';
import { useParams } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

const DEFAULT_INPUT_HEIGHT = '20px';
const INITIAL_CONTENT = '';

const useCommentInput = () => {
  const commentInputRef = useRef<HTMLTextAreaElement>(null);
  const [content, setContent] = useState(INITIAL_CONTENT);
  const queryClient = useQueryClient();
  const isLogin = useRecoilValue(isLoginState);
  const { cycleDetailId } = useParams();

  const { mutate: postComment } = usePostComment(
    { cycleDetailId: Number(cycleDetailId) },
    {
      onSuccess: () => {
        invalidateQueries();

        setContent(INITIAL_CONTENT);

        resizeToInitialHeight();
      },
    },
  );

  const renderSnackBar = useSnackBar();

  const isWriteButtonDisabled = content.length === 0;

  const handleChangeInput = (event: ChangeEvent<HTMLTextAreaElement>) => {
    // 입력 값을 지웠을 때, 댓글 입력창의 높이를 줄이기 위한 코드
    event.target.style.height = DEFAULT_INPUT_HEIGHT;
    event.target.style.height = event.target.scrollHeight + 'px';

    setContent(event.target.value);
  };

  const handleClickWrite = () => {
    if (!isLogin) {
      renderSnackBar({
        message: '로그인한 사용자만 댓글을 작성할 수 있습니다. 로그인을 해주세요:)',
        status: 'ERROR',
        linkText: '로그인하러 가기',
        linkTo: CLIENT_PATH.HOME,
      });

      return;
    }

    postComment({ content });
  };

  const invalidateQueries = () => {
    queryClient.invalidateQueries(queryKeys.getFeedById);
    queryClient.invalidateQueries(queryKeys.getCommentsById);
  };

  const resizeToInitialHeight = () => {
    if (!commentInputRef.current) {
      return;
    }

    commentInputRef.current.style.height = DEFAULT_INPUT_HEIGHT;
  };

  return {
    commentInputRef,
    content,
    isWriteButtonDisabled,
    handleChangeInput,
    handleClickWrite,
  };
};

export default useCommentInput;
