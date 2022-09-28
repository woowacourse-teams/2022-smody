import { ValidatorFunction } from 'types/internal';

type checkFormatFunction = (value: string, optionalValue?: string) => boolean;

const checkEmailFormat: checkFormatFunction = (email) => {
  const emailRule = /^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;

  return !emailRule.test(email);
};

const checkEmailBlank: checkFormatFunction = (email) =>
  email.length !== email.trim().length;

const checkPasswordFormat: checkFormatFunction = (password) => {
  const passwordRule = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z0-9]{10,20}$/;

  return !passwordRule.test(password);
};

const checkNicknameFormat: checkFormatFunction = (nickname) => {
  const noSpaceRule = /\s/g;

  return noSpaceRule.test(nickname) || nickname.length < 1 || nickname.length > 10;
};

const checkSamePassword: checkFormatFunction = (password, passwordCheck) =>
  password !== passwordCheck;

const checkIntroductionFormat: checkFormatFunction = (introduction) =>
  introduction.length > 30;

const checkChallengeNameFormat: checkFormatFunction = (challengeName) => {
  const challengeNameLength = challengeName.trim().length;

  return (
    challengeNameLength !== challengeName.length ||
    challengeNameLength === 0 ||
    challengeNameLength > 30
  );
};

const checkChallengeDescriptionFormat: checkFormatFunction = (challengeDescription) => {
  const challengeDescriptionLength = challengeDescription.trim().length;

  return challengeDescriptionLength === 0 || challengeDescriptionLength > 255;
};

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
    return { isValidated: false, message: '10~20자 영문자, 숫자를 사용해주세요.' };
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
    return { isValidated: false, message: '1~10자로 공백 없이 입력해주세요.' };
  }

  return { isValidated: true, message: '사용 가능한 닉네임입니다.' };
};

export const validateIntroduction: ValidatorFunction = (introduction: string) => {
  if (checkIntroductionFormat(introduction)) {
    return { isValidated: false, message: '30자 이내로 입력해 주세요.' };
  }

  return { isValidated: true, message: '올바른 입력입니다.' };
};

export const validateChallengeName: ValidatorFunction = (challengeName: string) => {
  if (checkChallengeNameFormat(challengeName)) {
    return {
      isValidated: false,
      message: '앞 뒤 공백 없이 1~30자 이내로 입력해 주세요.',
    };
  }
  return { isValidated: true, message: '올바른 입력입니다.' };
};

export const validateChallengeDescription: ValidatorFunction = (
  challengeDescription: string,
) => {
  if (checkChallengeDescriptionFormat(challengeDescription)) {
    return {
      isValidated: false,
      message: '앞 뒤의 공백을 제외하고 255자 이내로 입력해 주세요.',
    };
  }
  return { isValidated: true, message: '올바른 입력입니다.' };
};
