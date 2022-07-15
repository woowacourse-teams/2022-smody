import { usePostLogin } from 'apis';
import { authApiClient } from 'apis/apiClient';
import ServiceExampleImage from 'assets/service_example.png';
import { useContext, MouseEventHandler } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { isLoginState, nicknameState } from 'recoil/auth/atoms';
import styled, { ThemeContext } from 'styled-components';

import { FlexBox, Text, FixedButton } from 'components';

import { CLIENT_PATH } from 'constants/path';

export const CertUnAuth = () => {
  const setNickname = useSetRecoilState(nicknameState);
  const setIsLogin = useSetRecoilState(isLoginState);

  const themeContext = useContext(ThemeContext);
  const navigate = useNavigate();
  const { mutate } = usePostLogin({
    onSuccess: ({ data: { nickname, accessToken } }) => {
      console.log(`반갑습니다, ${nickname}님!`);

      setIsLogin(true);
      setNickname(nickname);

      authApiClient.updateAuth(accessToken);
      navigate(CLIENT_PATH.CERT);
    },
    onError: () => {
      console.log('로그인 실패...');
      throw new Error();
    },
  });

  const handleClickLogin: MouseEventHandler<HTMLButtonElement> = () => {
    mutate({ email: 'test@gmail.com', password: '!12345abcd' });
  };

  return (
    <Wrapper>
      <ColumnWrapper>
        <Text color={themeContext.onBackground} size={24} fontWeight="bold">
          작심삼일에 지쳤을 때
        </Text>
        <RowWrapper>
          <Text color={themeContext.primary} size={24} fontWeight="bold">
            Three More Days
          </Text>
          <Text color={themeContext.onBackground} size={24} fontWeight="bold">
            ,&nbsp;
          </Text>
          <Text color={themeContext.onBackground} size={32} fontWeight="bold">
            Smody
          </Text>
        </RowWrapper>
      </ColumnWrapper>
      <ColumnWrapper>
        <RowWrapper>
          <Text color={themeContext.primary} size={24} fontWeight="bold">
            3일
          </Text>
          <Text color={themeContext.mainText} size={24} fontWeight="bold">
            간 진행할,
          </Text>
        </RowWrapper>
        <Text color={themeContext.mainText} size={24} fontWeight="bold">
          여러 챌린지를 확인해보세요
        </Text>
      </ColumnWrapper>
      <ServiceExample src={ServiceExampleImage} alt="서비스 예시 이미지" />
      <FixedButton onClick={handleClickLogin}>구글로 시작하기</FixedButton>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  justifyContent: 'space-between',
  alignItems: 'center',
  gap: '1rem',
})`
  padding-top: 5rem;
`;

const ColumnWrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  alignItems: 'center',
  gap: '0.5rem',
})``;

const RowWrapper = styled(FlexBox).attrs({
  flexDirection: 'row',
  flexWrap: 'wrap',
  alignItems: 'flex-end',
})``;

const ServiceExample = styled.img`
  margin-top: 3rem;
  width: 17.438rem;
  max-width: 350px;
  aspect-ratio: 0.56;
`;
