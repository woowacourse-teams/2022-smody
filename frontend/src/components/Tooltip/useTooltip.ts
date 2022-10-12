import { useState, MouseEventHandler } from 'react';

const useTooltip = () => {
  const [isOpenToolTip, setIsOpenToolTip] = useState(false);

  const openTooltip = () => {
    setIsOpenToolTip(true);
  };

  const closeTooltip: MouseEventHandler<HTMLButtonElement> = () => {
    setIsOpenToolTip(false);
  };

  const closeTooltipOnBg: MouseEventHandler<HTMLDivElement> = (event) => {
    if (event.currentTarget === event.target) {
      setIsOpenToolTip(false);
    }
  };

  return { isOpenToolTip, openTooltip, closeTooltip, closeTooltipOnBg };
};

export default useTooltip;
