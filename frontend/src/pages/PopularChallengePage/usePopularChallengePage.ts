import { useGetAllChallenges } from 'apis';

export const usePopularChallengePage = () => {
  const { data: challengeInfiniteData } = useGetAllChallenges({ sort: 'popular' });
  return { challengeInfiniteData };
};
