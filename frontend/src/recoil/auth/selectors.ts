import { AtomEffect } from 'recoil';
import { isLoginStateType } from 'recoil/auth/type';

export const isLoginStateLocalStorageEffect: AtomEffect<isLoginStateType> = ({
  setSelf,
}) => {
  const accessToken = localStorage.getItem('accessToken');

  setSelf(accessToken !== null);
};
