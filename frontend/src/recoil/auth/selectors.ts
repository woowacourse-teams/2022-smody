import { selector } from 'recoil';

export const isLoginState = selector({
  key: 'isLoginState',
  get: () => {
    const accessToken = localStorage.getItem('accessToken');

    if (accessToken) {
      return true;
    }
    return false;
  },
});
