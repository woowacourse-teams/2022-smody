import { useCallback } from 'react';
import { useSetRecoilState } from 'recoil';
import { snackBarState } from 'recoil/snackbar/atoms';

import { SnackBarProps } from 'components/@shared/SnackBar/type';

const useSnackBar = () => {
  const setSnackBar = useSetRecoilState(snackBarState);
  let timeoutId: null | number;

  const renderSnackBar = useCallback(
    ({ status, message, linkText, linkTo }: SnackBarProps) => {
      if (timeoutId) {
        clearTimeout(timeoutId);
        timeoutId = null;
        setSnackBar((prev) => {
          return { ...prev, isVisible: false };
        });
      }

      timeoutId = window.setTimeout(() => {
        setSnackBar((prev) => {
          return { ...prev, isVisible: false };
        });
      }, 3000);

      setSnackBar({ isVisible: true, status, message, linkText, linkTo });
    },
    [setSnackBar],
  );

  return renderSnackBar;
};

export default useSnackBar;
