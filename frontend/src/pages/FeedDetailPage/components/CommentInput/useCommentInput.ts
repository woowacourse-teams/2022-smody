import { UseCommentInputProps } from './type';
import useMention from './useMention';
import { queryKeys } from 'apis/constants';
import { usePatchComments, usePostComment } from 'apis/feedApi';
import { useState, useEffect, useRef } from 'react';
import { useQueryClient } from 'react-query';
import { useParams } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import useSnackBar from 'hooks/useSnackBar';

import { MAX_TEXTAREA_LENGTH } from 'constants/domain';
import { CLIENT_PATH } from 'constants/path';

const INITIAL_CONTENT = '';
const DEFAULT_INPUT_HEIGHT = '1.5rem';

const useCommentInput = ({
  selectedCommentId,
  editMode,
  turnOffEditMode,
}: UseCommentInputProps) => {
  // ---------- 댓글 작성 및 수정하여 db에 보내는 관련 로직 ----------------

  const [content, setContent] = useState(INITIAL_CONTENT);
  const queryClient = useQueryClient();
  const isLogin = useRecoilValue(isLoginState);
  const { cycleDetailId } = useParams();
  const renderSnackBar = useSnackBar();

  const commentInputRef = useRef<HTMLDivElement>(null);
  const [mentionedMemberIds, setMentionedMemberIds] = useState<Array<number>>([]);

  const {
    postMentionNotifications,
    isLoadingPostMentionNotifications,
    isPopoverOpen,
    handleClosePopover,
    membersData,
    hasNextMembersPage,
    fetchNextMembersPage,
    selectMember,
    handleKeydownCommentInput,
    handleInputCommentInput,
  } = useMention({
    commentInputRef,
    setContent,
    mentionedMemberIds,
    setMentionedMemberIds,
  });

  const { mutate: postComment, isLoading: isLoadingPostComment } = usePostComment(
    { cycleDetailId: Number(cycleDetailId) },
    {
      onSuccess: () => {
        invalidateQueries();

        setContent(INITIAL_CONTENT);

        commentInputRef.current!.textContent = '';

        setMentionedMemberIds([]);

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

      commentInputRef.current!.textContent = '';

      setMentionedMemberIds([]);

      resizeToInitialHeight();

      renderSnackBar({
        status: 'SUCCESS',
        message: '댓글이 수정됐습니다.',
      });

      turnOffEditMode();
    },
  });

  useEffect(() => {
    setContent(editMode.editContent);
    commentInputRef.current!.innerText = editMode.editContent;
  }, [editMode]);

  const isVisibleWriteButton = content.length !== 0;
  const isMaxLengthOver = content.length >= MAX_TEXTAREA_LENGTH - 1;
  const isEmptyContent = content.length !== 0 && content.trim() === '';

  const isCommentError = isMaxLengthOver || isEmptyContent;
  const commentErrorMessage = isMaxLengthOver
    ? '댓글은 최대 255자까지 입력할 수 있습니다.'
    : isEmptyContent
    ? '빈 댓글은 입력할 수 없습니다.'
    : '';

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

    postMentionNotifications({
      memberIds: Array.from(new Set(mentionedMemberIds)),
      pathId: Number(cycleDetailId),
    });
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
    isVisibleWriteButton,
    isCommentError,
    commentErrorMessage,
    isLoadingPostComment,
    isLoadingPatchComment,
    handleClickWrite,
    isLoadingPostMentionNotifications,
    isPopoverOpen,
    handleClosePopover,
    membersData,
    hasNextMembersPage,
    fetchNextMembersPage,
    selectMember,
    handleKeydownCommentInput,
    handleInputCommentInput,
  };
};

export default useCommentInput;
