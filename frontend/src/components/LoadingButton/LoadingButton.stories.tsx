import { LoadingButton } from 'components/LoadingButton';
import { LoadingButtonProps } from 'components/LoadingButton/type';

export default {
  title: 'components/LoadingButton',
  component: LoadingButton,
};

export const DefaultLoadingButton = (args: LoadingButtonProps) => (
  <LoadingButton {...args} />
);

DefaultLoadingButton.args = {
  isDisabled: true,
  isSuccess: false,
  isLoading: true,
  defaultText: '인증하기',
  loadingText: '업로드 중',
  successText: '인증완료',
};
