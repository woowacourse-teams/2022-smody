import { useGetMyChallengeById } from 'apis';

const useChallengeInfoWithUser = (challengeId: number) => {
  const { data: challengeData } = useGetMyChallengeById(
    { challengeId },
    {
      refetchOnWindowFocus: false,
    },
  );

  return challengeData;
};

export default useChallengeInfoWithUser;
