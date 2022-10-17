import { useState, MouseEventHandler } from 'react';

const useTooltip = () => {
  const [isOpenTooltip, setIsOpenTooltip] = useState(false);

  const openTooltip = () => {
    setIsOpenTooltip(true);
  };

  const closeTooltip: MouseEventHandler<HTMLButtonElement> = () => {
    setIsOpenTooltip(false);
  };

  const closeTooltipOnBg: MouseEventHandler<HTMLDivElement> = (event) => {
    if (event.currentTarget === event.target) {
      setIsOpenTooltip(false);
    }
  };

  return { isOpenTooltip, openTooltip, closeTooltip, closeTooltipOnBg };
};

export default useTooltip;
