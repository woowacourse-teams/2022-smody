import { useGetFeedById } from 'apis/feedApi';
import { useParams } from 'react-router-dom';

const useFeedDetailPage = () => {
  const { cycleDetailId } = useParams();
  const { data: feedDetailData } = useGetFeedById(
    {
      cycleDetailId: Number(cycleDetailId),
    },
    {
      refetchOnWindowFocus: false,
    },
  );

  return {
    feedDetailData,
  };
};

export default useFeedDetailPage;
