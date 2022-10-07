import { UseTitleProps } from './type';
import { useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';

import useScrollDirection from 'hooks/useScrollDirection';

const useTitle = ({ linkTo }: UseTitleProps) => {
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

  return { backToPreviousPage, TitleRef };
};

export default useTitle;
