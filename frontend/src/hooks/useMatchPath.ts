import { useLocation } from 'react-router-dom';

const useMatchPath = <T>(matchedData: T, unMatchedData: T) => {
  const { pathname } = useLocation();
  const getPathMatchResult = (routes: string[], exceptRoutes?: string[]) => {
    let matchResult = matchedData;
    if (exceptRoutes) {
      exceptRoutes.forEach((route) => {
        if (pathname.includes(route)) {
          matchResult = unMatchedData;
          return;
        }
      });
      if (matchResult === unMatchedData) {
        return matchResult;
      }
    }
    matchResult = unMatchedData;
    routes.forEach((route) => {
      if (pathname.includes(route)) {
        matchResult = matchedData;
        return;
      }
    });
    return matchResult;
  };

  return getPathMatchResult;
};

export default useMatchPath;
