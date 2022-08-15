import { useGetCommentsById, useGetFeedById } from 'apis/feedApi';
import { useState } from 'react';
import { useParams } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

const useFeedDetailPage = () => {
  const { cycleDetailId } = useParams();
  const isLogin = useRecoilValue(isLoginState);
  const [isMenuBottomSheetOpen, setIsMenuBottomSheetOpen] = useState(false);
  const [selectedCommentId, setSelectedCommentId] = useState<number | null>(null);
  const [editMode, setEditMode] = useState({
    isEditMode: false,
    editContent: '',
  });

  const { data: feedData } = useGetFeedById(
    {
      cycleDetailId: Number(cycleDetailId),
    },
    {
      refetchOnWindowFocus: false,
    },
  );

  const { data: commentsData } = useGetCommentsById(
    {
      cycleDetailId: Number(cycleDetailId),
      isLogin,
    },
    {
      refetchOnWindowFocus: false,
    },
  );

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
    // TODO: 댓글 삭제하기 API 요청
    console.log('댓글 삭제하기 요청: ', selectedCommentId);
    setIsMenuBottomSheetOpen(false);
  };

  const turnOffEditMode = () => {
    setEditMode({
      isEditMode: false,
      editContent: '',
    });
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
    handleClickMenuButton,
    handleCloseBottomSheet,
    handleClickCommentEdit,
    handleClickCommentDelete,
    turnOffEditMode,
  };
};

export default useFeedDetailPage;
