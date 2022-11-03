import { UseCardBoxProps } from './type';
import { useNavigate } from 'react-router-dom';

import { CLIENT_PATH } from 'constants/path';

export const useCardBox = ({ challengeId }: UseCardBoxProps) => {
  const navigate = useNavigate();

  const handleNavigateMyChallenge = () => {
    navigate(`${CLIENT_PATH.PROFILE_CHALLENGE_DETAIL}/${challengeId}`);
  };

  return handleNavigateMyChallenge;
};
