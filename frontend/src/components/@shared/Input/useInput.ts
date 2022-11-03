import { useState } from 'react';

export const useInput = () => {
  const [isFocus, setIsFocus] = useState(false);

  const handleFocus = () => {
    setIsFocus(true);
  };

  const handleBlur = () => {
    setIsFocus(false);
  };

  return {
    isFocus,
    handleFocus,
    handleBlur,
  };
};
