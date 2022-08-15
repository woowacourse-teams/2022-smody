import { useGetCommentsById, useGetFeedById } from 'apis/feedApi';
import { useState } from 'react';
import { useParams } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

const useFeedDetailPage = () => {
  const { cycleDetailId } = useParams();
  const isLogin = useRecoilValue(isLoginState);
  const [isMenuBottomSheetOpen, setIsMenuBottomSheetOpen] = useState(false);
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

  const handleClickMenuButton = () => {
    setIsMenuBottomSheetOpen(true);
  };

  const handleCloseBottomSheet = () => {
    setIsMenuBottomSheetOpen(false);
  };

  const handleClickCommentEdit = () => {
    // TODO: 댓글 수정하기 API 요청
  };

  const handleClickCommentDelete = () => {
    // TODO: 댓글 삭제하기 API 요청
  };

  return {
    feedData,
    commentsData,
    isMenuBottomSheetOpen,
    handleClickMenuButton,
    handleCloseBottomSheet,
    handleClickCommentEdit,
    handleClickCommentDelete,
  };
};

export default useFeedDetailPage;
