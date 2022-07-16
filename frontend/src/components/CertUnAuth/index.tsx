import { useGetLinkGoogle } from 'apis';
import ServiceExampleImage from 'assets/service_example.png';
import { useContext, MouseEventHandler } from 'react';
import styled, { ThemeContext } from 'styled-components';

import { FlexBox, Text, FixedButton } from 'components';

export const CertUnAuth = () => {
  const themeContext = useContext(ThemeContext);

  const { refetch: getLinkGoogle } = useGetLinkGoogle({
    enabled: false,
    onSuccess: ({ data: googleOAuthLoginLink }) => {
      const redirectionUrl =
        googleOAuthLoginLink + '&redirect_uri=http://localhost:3000/cert';
      window.location.href = redirectionUrl;
    },
  });

  const handleClickLogin: MouseEventHandler<HTMLButtonElement> = () => {
    getLinkGoogle();
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
