import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

const useDetectPageChange = <T>(func: (...args: T[]) => void) => {
  const { pathname } = useLocation();

  useEffect(() => {
    func();
  }, [pathname]);
};

export default useDetectPageChange;
