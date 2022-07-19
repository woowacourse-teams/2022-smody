import { atom } from 'recoil';

import { SnackBarProps } from 'components/SnackBar/type';

export const nicknameState = atom({
  key: 'nicknameState',
  default: '',
});

export const isLoginState = atom({
  key: 'isLoginState',
  default: false,
});

export const snackBarState = atom<SnackBarProps & { isVisible: boolean }>({
  key: 'snackBarState',
  default: {
    isVisible: false,
    message: '',
    status: 'SUCCESS',
    linkText: '',
    linkTo: '',
  },
});
