import { useGetCommentsById, useGetFeedById } from 'apis/feedApi';
import { useParams } from 'react-router-dom';

const useFeedDetailPage = () => {
  const { cycleDetailId } = useParams();
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
