import { useGetCommentsById, useGetFeedById } from 'apis/feedApi';
import { useParams } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

const useFeedDetailPage = () => {
  const { cycleDetailId } = useParams();
  const isLogin = useRecoilValue(isLoginState);

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

  return {
    feedData,
    commentsData,
  };
};

export default useFeedDetailPage;
