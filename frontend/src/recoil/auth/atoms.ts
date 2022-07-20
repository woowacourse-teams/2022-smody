import { atom } from 'recoil';

export const nicknameState = atom({
  key: 'nicknameState',
  default: '',
});

export const isLoginState = atom({
  key: 'isLoginState',
  default: false,
});
