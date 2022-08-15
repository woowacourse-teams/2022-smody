import { UseCommentInputProps } from './type';
import { queryKeys } from 'apis/constants';
import { usePostComment } from 'apis/feedApi';
import { useState, useEffect, useRef, ChangeEvent } from 'react';
import { useQueryClient } from 'react-query';
import { useParams } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import useSnackBar from 'hooks/useSnackBar';

import { MAX_TEXTAREA_LENGTH } from 'constants/domain';
import { CLIENT_PATH } from 'constants/path';

const DEFAULT_INPUT_HEIGHT = '1.5rem';
const INITIAL_CONTENT = '';

const useCommentInput = ({ editMode, turnOffEditMode }: UseCommentInputProps) => {
  const commentInputRef = useRef<HTMLTextAreaElement>(null);
  const [content, setContent] = useState(INITIAL_CONTENT);
  const queryClient = useQueryClient();
  const isLogin = useRecoilValue(isLoginState);
  const { cycleDetailId } = useParams();
  const renderSnackBar = useSnackBar();

  const { mutate: postComment, isLoading: isLoadingPostComment } = usePostComment(
    { cycleDetailId: Number(cycleDetailId) },
    {
      onSuccess: () => {
        invalidateQueries();

        setContent(INITIAL_CONTENT);

        resizeToInitialHeight();

        renderSnackBar({
          status: 'SUCCESS',
          message: '댓글이 작성됐습니다.',
        });
      },
    },
  );

  // TODO: 댓글 수정 API 연결

  useEffect(() => {
    setContent(editMode.editContent);
  }, [editMode]);

  const isVisibleWriteButton = content.length !== 0;
  const isShowLengthWarning = content.length >= MAX_TEXTAREA_LENGTH - 1;

  const handleChangeInput = (event: ChangeEvent<HTMLTextAreaElement>) => {
    const { target } = event;

    resizeHeight(target);
    setContent(target.value.slice(0, MAX_TEXTAREA_LENGTH));
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

    if (editMode.isEditMode) {
      // TODO: 수정하기에 따라 pathComment 요청보내기
      return;
    }
    postComment({ content });
  };

  const invalidateQueries = () => {
    queryClient.invalidateQueries(queryKeys.getFeedById);
    queryClient.invalidateQueries(queryKeys.getCommentsById);
  };

  const resizeHeight = (element: HTMLTextAreaElement) => {
    // 입력 값을 지웠을 때, 댓글 입력창의 높이를 줄이기 위한 코드
    element.style.height = DEFAULT_INPUT_HEIGHT;
    element.style.height = element.scrollHeight + 'px';
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
    isVisibleWriteButton,
    isShowLengthWarning,
    isLoadingPostComment,
    handleChangeInput,
    handleClickWrite,
  };
};

export default useCommentInput;
