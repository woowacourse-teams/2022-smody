import { ValidatorFunction } from 'commonType';

type checkFormatFunction = (value: string, optionalValue?: string) => boolean;

const checkEmailFormat: checkFormatFunction = (email) =>
  !email.includes('@') || !email.includes('.', email.indexOf('@'));

const checkEmailBlank: checkFormatFunction = (email) =>
  email.length !== email.trim().length;

const checkPasswordFormat: checkFormatFunction = (password) => {
  const passwordRule = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z0-9]{10,20}$/;

  return !passwordRule.test(password);
};

const checkNicknameFormat: checkFormatFunction = (nickname) =>
  nickname.length < 2 || nickname.length > 10;

const checkSamePassword: checkFormatFunction = (password, passwordCheck) =>
  password !== passwordCheck;

export const validateEmail: ValidatorFunction = (email) => {
  if (checkEmailFormat(email)) {
    return { isValidated: false, message: '올바른 이메일 형식을 입력해주세요.' };
  }

  if (checkEmailBlank(email)) {
    return { isValidated: false, message: '공백없이 이메일을 적어주세요.' };
  }

  return { isValidated: true, message: '사용 가능한 이메일입니다.' };
};

export const validatePassword: ValidatorFunction = (password) => {
  if (checkPasswordFormat(password)) {
    return { isValidated: false, message: '올바른 비밀번호 형식을 입력해주세요.' };
  }

  return { isValidated: true, message: '사용 가능한 비밀번호입니다.' };
};

export const validateSamePassword: ValidatorFunction = (password, passwordCheck) => {
  if (checkSamePassword(password, passwordCheck)) {
    return { isValidated: false, message: '비밀번호가 일치하지 않습니다.' };
  }

  return { isValidated: true, message: '비밀번호가 일치합니다.' };
};

export const validateNickname: ValidatorFunction = (nickname: string) => {
  if (checkNicknameFormat(nickname)) {
    return { isValidated: false, message: '올바른 닉네임 형식을 입력해주세요.' };
  }

  return { isValidated: true, message: '사용 가능한 닉네임입니다.' };
};
