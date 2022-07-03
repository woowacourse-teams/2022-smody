import styled from 'styled-components';

import { Text } from 'components/@shared/Text';

import { Input } from 'components/Input';

const SignUpPage = () => {
  return (
    <Container>
      {/* <img src={ } alt='로고' width='100px' height='60px' /> */}

      <Text size={24} color="#000000">
        회원가입
      </Text>
      <Input
        label="이메일"
        type="text"
        value=""
        onChange={(e) => {}}
        placeholder="이것은 이메일"
        message=""
      />
      {/* <Input />
      <Input />
      <Input />

      <Button />
      <Text /> */}
    </Container>
  );
};

const Container = styled.main``;

export default SignUpPage;
