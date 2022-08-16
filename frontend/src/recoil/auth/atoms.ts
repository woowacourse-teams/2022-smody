import { atom, selector } from 'recoil';

export const nicknameState = atom({
  key: 'nicknameState',
  default: '',
});

export const isLoginState = selector({
  key: 'isLoginState',
  get: () => {
    const accessToken = localStorage.getItem('accessToken');
    return !!accessToken;
  },
});
