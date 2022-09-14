import { useRef } from 'react';

const useDebounce = (delay = 500) => {
  const timerId = useRef<null | NodeJS.Timeout>(null);

  const debounce = (callback: () => void) => {
    if (timerId.current !== null) {
      clearTimeout(timerId.current);
      timerId.current = null;
    }

    timerId.current = setTimeout(callback, delay);
  };

  return {
    debounce,
  };
};

export default useDebounce;
