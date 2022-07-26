import { useGetTokenGoogle } from 'apis';
import { authApiClient } from 'apis/apiClient';
import ServiceExampleImage from 'assets/service_example.png';
import { useContext } from 'react';
import { useEffect } from 'react';
import { useSetRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import styled, { ThemeContext, keyframes } from 'styled-components';
import { getUrlParameter } from 'utils';

import { useGoogleLogin } from 'hooks/useGoogleLogin';
import useSnackBar from 'hooks/useSnackBar';

import { FlexBox, Text, FixedButton } from 'components';

export const LandingPage = () => {
  const themeContext = useContext(ThemeContext);
  const renderSnackBar = useSnackBar();
  const setIsLogin = useSetRecoilState(isLoginState);

  const googleCode = getUrlParameter('code');

  const { refetch: getTokenGoogle } = useGetTokenGoogle(googleCode, {
    enabled: false,
    onSuccess: ({ data: { accessToken } }) => {
      authApiClient.updateAuth(accessToken);
      setIsLogin(true);

      renderSnackBar({
        message: '환영합니다 🎉 오늘 도전도 화이팅!',
        status: 'SUCCESS',
      });
    },
  });

  useEffect(() => {
    if (googleCode.length === 0) {
      return;
    }

    getTokenGoogle();
  });
  const getLinkGoogle = useGoogleLogin();

  return (
    <Wrapper>
      <ColumnWrapper>
        <Text color={themeContext.onBackground} size={20} fontWeight="bold">
          작심삼일에 지쳤을 때
        </Text>
        <RowWrapper>
          <Text color={themeContext.primary} size={20} fontWeight="bold">
            Three More Days
          </Text>
          <Text color={themeContext.onBackground} size={20} fontWeight="bold">
            ,&nbsp;
          </Text>
          <Text color={themeContext.onBackground} size={32} fontWeight="bold">
            Smody
          </Text>
        </RowWrapper>
      </ColumnWrapper>
      <ColumnWrapper>
        <RowWrapper>
          <Text color={themeContext.primary} size={20} fontWeight="bold">
            3일
          </Text>
          <Text color={themeContext.mainText} size={20} fontWeight="bold">
            간 진행할,
          </Text>
        </RowWrapper>
        <Text color={themeContext.mainText} size={20} fontWeight="bold">
          여러 챌린지를 확인해보세요
        </Text>
      </ColumnWrapper>
      <ServiceExample src={ServiceExampleImage} alt="서비스 예시 이미지" />
      <FixedButton onClick={getLinkGoogle}>구글로 시작하기</FixedButton>
    </Wrapper>
  );
};

const fadeIn = keyframes`
  0% {
    opacity: 0;
    transform: translate3d(0, 50px, 0);
  }
  100% {
    opacity: 1;
    transform: translate3d(0, 0, 0);
  }
`;

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
})`
  animation: 0.5s ease-in-out 0s 1 normal forwards running ${fadeIn};
`;

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
  animation: 0.5s ease-in-out 0s 1 normal forwards running ${fadeIn};
`;
