import { useGetMyChallengeById } from 'apis';
import { useParams } from 'react-router-dom';

const useChallengeInfoWithUser = () => {
  const { challengeId } = useParams();

  const { data: challengeData } = useGetMyChallengeById(
    { challengeId: Number(challengeId) },
    {
      refetchOnWindowFocus: false,
    },
  );

  return challengeData;
};

export default useChallengeInfoWithUser;
