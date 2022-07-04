import EmailIcon from 'assets/email_icon.svg';
import PersonIcon from 'assets/person_icon.svg';
import PasswordIcon from 'assets/pw_icon.svg';
import styled from 'styled-components';
import {
  validateEmail,
  validateNickname,
  validatePassword,
  validateSamePassword,
} from 'utils/validator';

import useInput from 'hooks/useInput';

import { FlexBox } from 'components/@shared/FlexBox';
import { Text } from 'components/@shared/Text';

import { AuthInput } from 'components/AuthInput';
import { Button } from 'components/Button';

const SignUpPage = () => {
  const email = useInput('', validateEmail);
  const nickname = useInput('', validateNickname);
  const password = useInput('', validatePassword);
  const passwordCheck = useInput('', validateSamePassword, password.value);
  const isAllValidated =
    email.isValidated &&
    nickname.isValidated &&
    password.isValidated &&
    passwordCheck.isValidated;

  const onSubmit = () => {};

  return (
    <Wrapper>
      <Text size={24} color="#000000">
        회원가입
      </Text>
      <Form onSubmit={onSubmit}>
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
    </Wrapper>
  );
};

const Wrapper = styled.main``;
const Form = styled(FlexBox).attrs({
  flexDirection: 'column',
  gap: '20px',
})``;

export default SignUpPage;
