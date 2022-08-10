import { useGetCycleById } from 'apis';
import { useParams } from 'react-router-dom';

const useCycleDetailPage = () => {
  const { cycleId } = useParams();
  const { data: cycleDetailData } = useGetCycleById(
    { cycleId: Number(cycleId) },
    {
      refetchOnWindowFocus: false,
    },
  );

  return cycleDetailData;
};

export default useCycleDetailPage;
