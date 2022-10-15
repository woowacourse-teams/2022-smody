import { UseTitleProps } from './type';
import { useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';

import useMatchPath from 'hooks/useMatchPath';
import useScrollDirection from 'hooks/useScrollDirection';
import useThemeContext from 'hooks/useThemeContext';

import { CLIENT_PATH } from 'constants/path';

const useTitle = ({ linkTo }: UseTitleProps) => {
  const themeContext = useThemeContext();
  const navigate = useNavigate();
  const backToPreviousPage = () => {
    if (linkTo) {
      navigate(linkTo);
      return;
    }

    navigate(-1);
  };
  const TitleRef = useRef<HTMLDivElement>(null);
  const scrollDirection = useScrollDirection();
  const getPathMatchColor = useMatchPath(themeContext.secondary, themeContext.background);

  const bgColor = getPathMatchColor([CLIENT_PATH.CERT, CLIENT_PATH.CYCLE_DETAIL]);

  useEffect(() => {
    if (!TitleRef || !TitleRef.current) {
      return;
    }
    if (scrollDirection === 'up') {
      TitleRef.current.style.top = '3rem';

      return;
    }
    if (scrollDirection === 'down') {
      TitleRef.current.style.top = '0';

      return;
    }
  }, [scrollDirection]);

  return { backToPreviousPage, TitleRef, themeContext, bgColor };
};

export default useTitle;
