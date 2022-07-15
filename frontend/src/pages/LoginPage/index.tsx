import { usePostLogin } from 'apis';
import { authApiClient } from 'apis/apiClient';
import EmailIcon from 'assets/email_icon.svg';
import PasswordIcon from 'assets/pw_icon.svg';
import { useContext, FormEventHandler } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { isLoginState, nicknameState } from 'recoil/auth/atoms';
import { ThemeContext } from 'styled-components';
import styled from 'styled-components';

import useInput from 'hooks/useInput';

import { RouteLoginState } from 'pages/LoginPage/type';

import { FlexBox, Text, AuthInput, Button, LinkText } from 'components';

import { CLIENT_PATH } from 'constants/path';

export const LoginPage = () => {
  const setNickname = useSetRecoilState(nicknameState);
  const setIsLogin = useSetRecoilState(isLoginState);
  const themeContext = useContext(ThemeContext);
  const location = useLocation();
  const state = location.state as RouteLoginState;
  const signUpEmail = state?.email || '';

  const email = useInput(signUpEmail);
  const password = useInput('');

  const navigate = useNavigate();
  const { mutate } = usePostLogin({
    // onSuccess: ({ data: { nickname, accessToken } }) => {
    //   console.log(`반갑습니다, ${nickname}님!`);

    //   setIsLogin(true);
    //   setNickname(nickname);

    //   authApiClient.updateAuth(accessToken);
    //   navigate(CLIENT_PATH.CERT);
    // },
    onError: () => {
      console.log('로그인 실패...');
      throw new Error();
    },
  });

  const isAllValidated = email.value && password.value;

  const onSubmit: FormEventHandler<HTMLFormElement> = (event) => {
    event.preventDefault();
    // mutate({ email: email.value, password: password.value });
  };

  return (
    <Wrapper>
      <Text size={24} color={themeContext.onBackground} fontWeight="bold">
        로그인
      </Text>
      <Form as="form" onSubmit={onSubmit}>
        <AuthInput
          icon={<EmailIcon />}
          type="email"
          placeholder="이메일을 입력해주세요"
          {...email}
        />
        <AuthInput
          icon={<PasswordIcon />}
          type="password"
          placeholder="비밀번호을 입력해주세요"
          {...password}
        />
        <Button size="large" disabled={!isAllValidated}>
          로그인
        </Button>
      </Form>
      <LinkText size={16} color={themeContext.primary} to="/sign_up">
        회원가입
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
