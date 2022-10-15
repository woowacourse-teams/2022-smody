import { useRef, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { useRecoilState } from 'recoil';
import { isDarkState } from 'recoil/darkMode/atoms';
import { LocationState } from 'types/internal';

import useDetectPageChange from 'hooks/useDetectPageChange';
import useScrollDirection from 'hooks/useScrollDirection';

import { TITLE_USING_PAGE } from 'constants/style';

export const useHeader = () => {
  const wrapperRef = useRef<HTMLDivElement>(null);
  const [isDark, setIsDark] = useRecoilState(isDarkState);
  const scrollDirection = useScrollDirection();
  const { pathname, state } = useLocation();

  useEffect(() => {
    if (isNotNeedScrollAnimation || wrapperRef.current === null) {
      return;
    }

    if (scrollDirection === 'up') {
      wrapperRef.current.style.top = '0';
      return;
    }

    if (scrollDirection === 'down') {
      wrapperRef.current.style.top = '-3rem';
      return;
    }
  }, [scrollDirection]);

  const resetHeader = () => {
    if (wrapperRef.current === null) {
      return;
    }
    wrapperRef.current.style.top = '0';
  };

  useDetectPageChange(resetHeader);

  const locationState = state as LocationState;

  const isInCertFormPage = locationState !== null && locationState.isInCertFormPage;

  const isNotNeedScrollAnimation =
    !TITLE_USING_PAGE.some((route) => pathname.includes(route)) && !isInCertFormPage;

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
