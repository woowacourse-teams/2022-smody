import EmailIcon from 'assets/email_icon.svg';
import PasswordIcon from 'assets/pw_icon.svg';
import { useContext } from 'react';
import { FormEventHandler } from 'react';
import { ThemeContext } from 'styled-components';
import styled from 'styled-components';

import useInput from 'hooks/useInput';

import { FlexBox, Text, AuthInput, Button, LinkText } from 'components';

export const LoginPage = () => {
  const themeContext = useContext(ThemeContext);

  const email = useInput('');
  const password = useInput('');

  const isAllValidated = email.value && password.value;

  const onSubmit: FormEventHandler<HTMLFormElement> = (event) => {
    event.preventDefault();

    alert('로그인');
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
