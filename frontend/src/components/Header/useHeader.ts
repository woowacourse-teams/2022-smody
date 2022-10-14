import { useRef, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { useRecoilState } from 'recoil';
import { isDarkState } from 'recoil/darkMode/atoms';

import useScrollDirection from 'hooks/useScrollDirection';

export const useHeader = () => {
  const wrapperRef = useRef<HTMLDivElement>(null);
  const [isDark, setIsDark] = useRecoilState(isDarkState);
  const scrollDirection = useScrollDirection();
  const { pathname } = useLocation();

  useEffect(() => {
    if (isNotNeedScrollAnimation) {
      return;
    }

    if (scrollDirection === 'up') {
      wrapperRef.current!.style.transform = 'translateY(0rem)';

      return;
    }

    if (scrollDirection === 'down') {
      wrapperRef.current!.style.transform = 'translateY(-3rem)';

      return;
    }
  }, [scrollDirection]);

  const isNotNeedScrollAnimation = !pathname.startsWith('/feed/detail');

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
