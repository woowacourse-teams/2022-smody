import { useLocation } from 'react-router-dom';

const useMatchPath = <T>(matchedData: T, unMatchedData: T) => {
  const { pathname } = useLocation();
  const getPathMatchResult = (routes: string[]) => {
    let matchResult = unMatchedData;
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
