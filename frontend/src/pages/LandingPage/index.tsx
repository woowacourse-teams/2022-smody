import ServiceExampleImage from 'assets/service_example.png';
import styled, { keyframes } from 'styled-components';

<<<<<<< HEAD
import useInstallApp from 'hooks/useInstallApp';
=======
import useAuth from 'hooks/useAuth';
>>>>>>> d4de18d (fix: oAuth 로그인 후 redirect 시 화면 깜박임 및 서스펜스 관련 에러 해결)
import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text, FixedButton, Logo } from 'components';

const LandingPage = () => {
  const themeContext = useThemeContext();
<<<<<<< HEAD

  const { installApp } = useInstallApp();
=======
  const { redirectGoogleLoginLink } = useAuth();

  const isLogin = useRecoilValue(isLoginState);
>>>>>>> d4de18d (fix: oAuth 로그인 후 redirect 시 화면 깜박임 및 서스펜스 관련 에러 해결)

  return (
    <Wrapper
      flexDirection="column"
      justifyContent="space-between"
      alignItems="center"
      gap="1rem"
    >
      <ColumnWrapper flexDirection="column" alignItems="center" gap="0.5rem">
        <Text color={themeContext.onBackground} size={20} fontWeight="bold">
          작심삼일에 지쳤을 때
        </Text>
        <FlexBox flexDirection="row" flexWrap="wrap" alignItems="flex-end">
          <Text color={themeContext.primary} size={20} fontWeight="bold">
            Three More Days
          </Text>
          <Text color={themeContext.onBackground} size={20} fontWeight="bold">
            ,&nbsp;
          </Text>
          <Logo isAnimated={true} width="100" color={themeContext.primary} />
        </FlexBox>
      </ColumnWrapper>
      <ColumnWrapper flexDirection="column" alignItems="center" gap="0.5rem">
        <FlexBox flexDirection="row" flexWrap="wrap" alignItems="flex-end">
          <Text color={themeContext.primary} size={20} fontWeight="bold">
            3
          </Text>
          <Text color={themeContext.onBackground} size={20} fontWeight="bold">
            일간 진행할,
          </Text>
        </FlexBox>
        <Text color={themeContext.onBackground} size={20} fontWeight="bold">
          여러 챌린지를 확인해보세요
        </Text>
      </ColumnWrapper>
      <ServiceExample src={ServiceExampleImage} alt="서비스 예시 이미지" />
      <FixedButton onClick={installApp}>앱 설치하기</FixedButton>
    </Wrapper>
  );
};

export default LandingPage;

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

const Wrapper = styled(FlexBox)`
  padding-top: 5rem;
`;

const ColumnWrapper = styled(FlexBox)`
  animation: 0.5s ease-in-out 0s 1 normal forwards running ${fadeIn};
`;

const ServiceExample = styled.img`
  margin-top: 3rem;
  width: 17.438rem;
  max-width: 350px;
  aspect-ratio: 0.56;
  animation: 0.5s ease-in-out 0s 1 normal forwards running ${fadeIn};
`;
