import { useLocation } from 'react-router-dom';

import { AvailablePickedColor } from 'styles/type';

const useMatchPath = (
  matchedColor: AvailablePickedColor,
  unMatchedColor: AvailablePickedColor,
) => {
  const { pathname } = useLocation();
  const getPathMatchResult = (routes: string[]) => {
    let matchResult = unMatchedColor;
    routes.forEach((route) => {
      if (pathname.includes(route)) {
        matchResult = matchedColor;
        return;
      }
    });
    return matchResult;
  };

  return getPathMatchResult;
};

export default useMatchPath;
