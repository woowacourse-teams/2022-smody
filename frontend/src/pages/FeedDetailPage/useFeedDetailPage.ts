import { useDeleteComments, useGetCommentsById, useGetFeedById } from 'apis/feedApi';
import { useState } from 'react';
import { useParams } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import useSnackBar from 'hooks/useSnackBar';

const useFeedDetailPage = () => {
  const { cycleDetailId } = useParams();
  const isLogin = useRecoilValue(isLoginState);
  const [isMenuBottomSheetOpen, setIsMenuBottomSheetOpen] = useState(false);
  const [selectedCommentId, setSelectedCommentId] = useState<number | null>(null);
  const [editMode, setEditMode] = useState({
    isEditMode: false,
    editContent: '',
  });
  const renderSnackBar = useSnackBar();

  const { data: feedData, refetch: refetchGetFeedById } = useGetFeedById(
    {
      cycleDetailId: Number(cycleDetailId),
    },
    {
      refetchOnWindowFocus: false,
    },
  );

  const { data: commentsData, refetch: refetchGetCommentsById } = useGetCommentsById(
    {
      cycleDetailId: Number(cycleDetailId),
      isLogin,
    },
    {
      refetchOnWindowFocus: false,
    },
  );

  const { mutate: deleteComment, isLoading: isLoadingDeleteComment } = useDeleteComments({
    onSuccess: () => {
      refetchQueries();
      setSelectedCommentId(null);
      setIsMenuBottomSheetOpen(false);

      renderSnackBar({
        status: 'SUCCESS',
        message: '댓글이 삭제됐습니다.',
      });
    },
  });

  const refetchQueries = () => {
    refetchGetFeedById();
    refetchGetCommentsById();
  };

  const handleClickMenuButton = (commentId: number) => {
    setIsMenuBottomSheetOpen(true);
    setSelectedCommentId(commentId);
  };

  const handleCloseBottomSheet = () => {
    setIsMenuBottomSheetOpen(false);
    setSelectedCommentId(null);
  };

  const handleClickCommentEdit = () => {
    const selectedCommentContent = findCommentContentById();

    setIsMenuBottomSheetOpen(false);
    setEditMode({
      isEditMode: true,
      editContent: selectedCommentContent,
    });
  };

  const handleClickCommentDelete = () => {
    if (typeof selectedCommentId === 'number') {
      deleteComment({ commentId: selectedCommentId });
    }
  };

  const turnOffEditMode = () => {
    setEditMode({
      isEditMode: false,
      editContent: '',
    });

    setSelectedCommentId(null);
  };

  const findCommentContentById = () => {
    const result = commentsData?.data.find(
      ({ commentId }) => commentId === selectedCommentId,
    );

    if (result === undefined) {
      return '';
    }

    return result.content;
  };

  return {
    feedData,
    commentsData,
    isMenuBottomSheetOpen,
    selectedCommentId,
    editMode,
    isLoadingDeleteComment,
    handleClickMenuButton,
    handleCloseBottomSheet,
    handleClickCommentEdit,
    handleClickCommentDelete,
    turnOffEditMode,
  };
};

export default useFeedDetailPage;
