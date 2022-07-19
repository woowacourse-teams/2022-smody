import { SnackBar } from 'components/SnackBar';
import { SnackBarProps } from 'components/SnackBar/type';

const snackBarRoot = document.createElement('div');
snackBarRoot.setAttribute('id', 'snackbar-root');
document.body.append(snackBarRoot);

export default {
  title: 'Components/SnackBar',
  component: SnackBar,
};

export const SuccessSnackBar = (args: SnackBarProps) => <SnackBar {...args} />;

SuccessSnackBar.args = {
  message: '성공하셨습니다!',
  status: 'SUCCESS',
};

export const ErrorSnackBar = (args: SnackBarProps) => <SnackBar {...args} />;

ErrorSnackBar.args = {
  message: '실패하였습니다!',
  status: 'ERROR',
};
