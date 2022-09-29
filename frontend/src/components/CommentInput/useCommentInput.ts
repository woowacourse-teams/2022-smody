import { UseCommentInputProps } from './type';
import { queryKeys } from 'apis/constants';
import { usePatchComments, usePostComment } from 'apis/feedApi';
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

const config = {
  subtree: true,
  characterData: true,
  childList: true,
};

const useCommentInput = ({
  selectedCommentId,
  editMode,
  turnOffEditMode,
}: UseCommentInputProps) => {
  const commentInputRef = useRef<HTMLDivElement>(null);
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

  const { mutate: patchComment, isLoading: isLoadingPatchComment } = usePatchComments({
    onSuccess: () => {
      invalidateQueries();

      setContent(INITIAL_CONTENT);

      resizeToInitialHeight();

      renderSnackBar({
        status: 'SUCCESS',
        message: '댓글이 수정됐습니다.',
      });

      turnOffEditMode();
    },
  });

  useEffect(() => {
    const observer = new MutationObserver(inputChangeHandler);

    observer.observe(commentInputRef.current!, config);

    return () => {
      observer.disconnect();
    };
  }, []);

  useEffect(() => {
    setContent(editMode.editContent);
  }, [editMode]);

  const isVisibleWriteButton = content.length !== 0;
  const isMaxLengthOver = content.length >= MAX_TEXTAREA_LENGTH - 1;

  const inputChangeHandler: MutationCallback = (mutations) => {
    const commentInputElement = commentInputRef.current!;
    resizeHeight(commentInputElement);
    const { innerText } = commentInputElement;

    setContent(innerText.slice(0, MAX_TEXTAREA_LENGTH));
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

    if (editMode.isEditMode && typeof selectedCommentId === 'number') {
      patchComment({ commentId: selectedCommentId, content });
      return;
    }
    postComment({ content });
  };

  const invalidateQueries = () => {
    queryClient.invalidateQueries(queryKeys.getFeedById);
    queryClient.invalidateQueries(queryKeys.getCommentsById);
  };

  const resizeHeight = (element: HTMLDivElement) => {
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
    isMaxLengthOver,
    isLoadingPostComment,
    isLoadingPatchComment,
    handleClickWrite,
  };
};

export default useCommentInput;
