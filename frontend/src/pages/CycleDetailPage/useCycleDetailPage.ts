import { useGetCycleById } from 'apis';
import { useParams } from 'react-router-dom';

const useCycleDetailPage = () => {
  const { cycleId } = useParams();
  const { data } = useGetCycleById(
    { cycleId: Number(cycleId) },
    {
      refetchOnWindowFocus: false,
    },
  );

  return data;
};

export default useCycleDetailPage;
