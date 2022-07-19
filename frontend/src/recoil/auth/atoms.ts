import { atom } from 'recoil';

export const nicknameState = atom({
  key: 'nicknameState',
  default: '',
});

export const isLoginState = atom({
  key: 'isLoginState',
  default: false,
});

export const snackBarState = atom<{
  isVisible: boolean;
  message: string;
  status: 'SUCCESS' | 'ERROR';
}>({
  key: 'snackBarState',
  default: {
    isVisible: false,
    message: '',
    status: 'SUCCESS',
  },
});
