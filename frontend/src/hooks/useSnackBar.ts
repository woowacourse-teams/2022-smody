import { useSetRecoilState } from 'recoil';
import { snackBarState } from 'recoil/auth/atoms';

import { SnackBarProps } from 'components/SnackBar/type';

const useSnackBar = () => {
  const setSnackBar = useSetRecoilState(snackBarState);
  const renderSnackbar = ({ message, status }: SnackBarProps) => {
    setSnackBar({ isVisible: true, message, status });

    setTimeout(() => setSnackBar((prev) => ({ ...prev, isVisible: false })));
  };

  return renderSnackbar;
};

export default useSnackBar;
