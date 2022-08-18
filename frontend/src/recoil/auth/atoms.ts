import { atom } from 'recoil';
import { isLoginStateLocalStorageEffect } from 'recoil/auth/selectors';

export const nicknameState = atom({
  key: 'nicknameState',
  default: '',
});

export const isLoginState = atom({
  key: 'isLoginState',
  effects: [isLoginStateLocalStorageEffect],
});
