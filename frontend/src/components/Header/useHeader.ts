import { useRef, useEffect } from 'react';
import { useRecoilState } from 'recoil';
import { isDarkState } from 'recoil/darkMode/atoms';

import useScrollDirection from 'hooks/useScrollDirection';

export const useHeader = () => {
  const wrapperRef = useRef<HTMLDivElement>(null);
  const [isDark, setIsDark] = useRecoilState(isDarkState);
  const scrollDirection = useScrollDirection();

  useEffect(() => {
    if (scrollDirection === 'up') {
      wrapperRef.current!.style.top = '0rem';

      return;
    }
    if (scrollDirection === 'down') {
      wrapperRef.current!.style.top = '-3rem';

      return;
    }
  }, [scrollDirection]);

  const handleDarkToggle = () => {
    localStorage.setItem('isDark', JSON.stringify(!isDark));
    setIsDark((prev) => !prev);
  };

  return {
    wrapperRef,
    isDark,
    handleDarkToggle,
  };
};
