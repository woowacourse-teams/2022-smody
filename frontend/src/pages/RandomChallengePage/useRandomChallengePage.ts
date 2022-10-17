import { useGetAllChallenges } from 'apis';

export const useRandomChallengePage = () => {
  const { data: challengeInfiniteData } = useGetAllChallenges({ sort: 'random' });
  return { challengeInfiniteData };
};
