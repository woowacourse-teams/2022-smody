import { useGetMyCyclesInProgress } from 'apis';

const useCertPage = () => {
  const { data } = useGetMyCyclesInProgress({
    refetchOnWindowFocus: false,
  });

  const cycles = data?.data;

  return { cycles };
};

export default useCertPage;
