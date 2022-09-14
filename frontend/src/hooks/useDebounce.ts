import { useRef } from 'react';

interface UseDebounceProps {
  callback: () => void;
  delay?: number;
}

const useDebounce = ({ callback, delay = 500 }: UseDebounceProps) => {
  const timerId = useRef<null | NodeJS.Timeout>(null);

  const debounce = () => {
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
