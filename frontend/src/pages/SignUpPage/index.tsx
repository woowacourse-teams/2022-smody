import EmailIcon from 'assets/email_icon.svg';
import PersonIcon from 'assets/person_icon.svg';
import PasswordIcon from 'assets/pw_icon.svg';
import { useState } from 'react';
import { ChangeEvent } from 'react';
import styled from 'styled-components';
import {
  validateEmail,
  validateNickname,
  validatePassword,
  validateSamePassword,
} from 'utils/validator';

import useInput from 'hooks/useInput';

import { Text } from 'components/@shared/Text';

import { AuthInput } from 'components/AuthInput';

const SignUpPage = () => {
  const email = useInput('', validateEmail);
  const nickname = useInput('', validateNickname);
  const password = useInput('', validatePassword);
  const passwordCheck = useInput('', validateSamePassword, password.value);

  return (
    <Wrapper>
      <Text size={24} color="#000000">
        회원가입
      </Text>
      <Container>
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
      </Container>
    </Wrapper>
  );
};

const Wrapper = styled.main``;
const Container = styled.main``;

export default SignUpPage;
