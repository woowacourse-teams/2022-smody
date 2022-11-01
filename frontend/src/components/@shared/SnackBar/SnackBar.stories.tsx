import { SnackBarProps } from './type';
import { useSetRecoilState } from 'recoil';
import { snackBarState } from 'recoil/snackbar/atoms';

import { SnackBar } from 'components/@shared/SnackBar';

const snackBarRoot = document.createElement('div');
snackBarRoot.setAttribute('id', 'snackbar-root');
document.body.append(snackBarRoot);

export default {
  title: 'Components/SnackBar',
  component: SnackBar,
  argTypes: {
    status: {
      options: ['SUCCESS', 'ERROR'],
      control: { type: 'select' },
    },
  },
};

export const DefaultSnackBar = (args: SnackBarProps & { isVisible: boolean }) => {
  const setSnackBar = useSetRecoilState(snackBarState);
  setSnackBar({
    ...args,
  });

  return <SnackBar />;
};

DefaultSnackBar.args = {
  isVisible: true,
  status: 'SUCCESS',
  message: '성공했어요',
  linkText: '더보기',
  linkTo: '/',
};
