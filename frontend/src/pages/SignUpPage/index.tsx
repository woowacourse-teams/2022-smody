import { usePostSignUp } from 'apis';
import EmailIcon from 'assets/email_icon.svg';
import PersonIcon from 'assets/person_icon.svg';
import PasswordIcon from 'assets/pw_icon.svg';
import { useContext, FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { ThemeContext } from 'styled-components';
import styled from 'styled-components';
import {
  validateEmail,
  validateNickname,
  validatePassword,
  validateSamePassword,
} from 'utils/validator';

import useInput from 'hooks/useInput';

import { FlexBox, Text, AuthInput, Button, LinkText } from 'components';

import { CLIENT_PATH } from 'constants/path';

export const SignUpPage = () => {
  const themeContext = useContext(ThemeContext);

  const email = useInput('', validateEmail);
  const nickname = useInput('', validateNickname);
  const password = useInput('', validatePassword);
  const passwordCheck = useInput('', validateSamePassword, password.value);
  const isAllValidated =
    email.isValidated &&
    nickname.isValidated &&
    password.isValidated &&
    passwordCheck.isValidated;

  const navigate = useNavigate();
  const { mutate } = usePostSignUp({
    onSuccess: ({ data: { email } }) => {
      alert('회원가입 성공!!');
      navigate(CLIENT_PATH.LOGIN, {
        state: {
          email,
        },
      });
    },
    onError: () => {
      alert('회원가입 실패...');
    },
  });

  const handleSubmitSignUp = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    mutate({ email: email.value, nickname: nickname.value, password: password.value });
  };

  return (
    <Wrapper>
      <Text size={24} color={themeContext.onBackground} fontWeight="bold">
        회원가입
      </Text>
      <Form as="form" onSubmit={handleSubmitSignUp}>
        <AuthInput
          icon={<EmailIcon />}
          type="email"
          label="이메일"
          placeholder="이메일을 입력해주세요"
          {...email}
        />
        <AuthInput
          icon={<PersonIcon />}
          type="text"
          label="닉네임"
          placeholder="닉네임을 입력해주세요"
          {...nickname}
        />
        <AuthInput
          icon={<PasswordIcon />}
          type="password"
          label="비밀번호"
          placeholder="비밀번호을 입력해주세요"
          {...password}
        />
        <AuthInput
          icon={<PasswordIcon />}
          type="password"
          label="비밀번호 재확인"
          placeholder="비밀번호를 다시 입력해 주세요."
          {...passwordCheck}
        />
        <Button size="large" disabled={!isAllValidated}>
          가입하기
        </Button>
      </Form>
      <LinkText size={16} color={themeContext.primary} to="/login">
        로그인
      </LinkText>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
})`
  ${Text} {
    margin-bottom: 2rem;
    align-self: center;
  }
`;

const Form = styled(FlexBox).attrs({
  flexDirection: 'column',
  gap: '1rem',
})`
  margin-bottom: 2rem;

  ${Button} {
    margin-top: 1rem;
  }
`;
