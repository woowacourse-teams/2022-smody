import { useGetMyCyclesInProgress } from 'apis';

const useCertPage = () => {
  const { data } = useGetMyCyclesInProgress();

  const cycles = data?.data;

  return { cycles };
};

export default useCertPage;
