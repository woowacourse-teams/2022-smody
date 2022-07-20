import { useCallback } from 'react';
import { useSetRecoilState } from 'recoil';
import { snackBarState } from 'recoil/snackbar/atoms';

import { SnackBarProps } from 'components/SnackBar/type';

const useSnackBar = () => {
  const setSnackBar = useSetRecoilState(snackBarState);
  let timeoutId: NodeJS.Timeout | null | number;

  const renderSnackBar = useCallback(
    ({ status, message, linkText, linkTo }: SnackBarProps) => {
      if (timeoutId) {
        clearTimeout(timeoutId);
        timeoutId = null;
        setSnackBar((prev) => {
          return { ...prev, isVisible: false };
        });
      }

      timeoutId = setTimeout(() => {
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
