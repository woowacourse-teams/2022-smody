import { UseUserRankingProps } from './type';
import { useGetMyRanking } from 'apis';

const useUserRanking = ({ rankingPeriodId }: UseUserRankingProps) => {
  const { data: myRankingData } = useGetMyRanking({ rankingPeriodId });

  return { myRankingData };
};

export default useUserRanking;
