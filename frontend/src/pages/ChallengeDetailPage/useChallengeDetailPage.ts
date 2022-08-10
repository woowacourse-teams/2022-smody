import { useGetChallengeById } from 'apis';
import { queryKeys } from 'apis/constants';
import { useQueryClient } from 'react-query';
import { useParams } from 'react-router-dom';
import { getEmoji } from 'utils/emoji';

import usePostJoinChallenge from 'hooks/usePostJoinChallenge';

const useChallengeDetailPage = () => {
  const queryClient = useQueryClient();
  const { challengeId } = useParams();
  const emoji = getEmoji(Number(challengeId));

  const { data } = useGetChallengeById(
    { challengeId: Number(challengeId) },
    {
      refetchOnWindowFocus: false,
    },
  );

  const { joinChallenge } = usePostJoinChallenge({
    challengeId: Number(challengeId),
    successCallback: () => {
      queryClient.invalidateQueries(queryKeys.getChallengeById);
    },
  });

  return { data, joinChallenge, emoji };
};

export default useChallengeDetailPage;
