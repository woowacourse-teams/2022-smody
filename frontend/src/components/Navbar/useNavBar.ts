import useMatchPath from 'hooks/useMatchPath';
import useThemeContext from 'hooks/useThemeContext';

import { CLIENT_PATH } from 'constants/path';

export const useNavBar = () => {
  const themeContext = useThemeContext();
  const getPathMatchResult = useMatchPath(themeContext.primary, themeContext.disabled);

  const certColor = getPathMatchResult([CLIENT_PATH.CERT, CLIENT_PATH.CYCLE_DETAIL]);
  const challengeColor = getPathMatchResult([
    CLIENT_PATH.SEARCH,
    CLIENT_PATH.CHALLENGE_DETAIL,
    CLIENT_PATH.CHALLENGE_CREATE,
  ]);
  const feedColor = getPathMatchResult([CLIENT_PATH.FEED]);
  const profileColor = getPathMatchResult([
    CLIENT_PATH.LOGIN,
    CLIENT_PATH.SIGN_UP,
    CLIENT_PATH.PROFILE,
  ]);
  const rankColor = getPathMatchResult([CLIENT_PATH.RANK]);

  return {
    certColor,
    challengeColor,
    feedColor,
    profileColor,
    rankColor,
  };
};
