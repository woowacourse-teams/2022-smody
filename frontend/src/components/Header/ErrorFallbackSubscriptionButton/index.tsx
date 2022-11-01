import { ErrorFallbackSubscriptionButtonProps } from './type';
import styled from 'styled-components';

import { useErrorFallbackLogic } from 'hooks/useErrorFallbackLogic';
import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, ToggleButton, UnderLineText } from 'components';

const handleClickToggleButton = () => {};

export const ErrorFallbackSubscriptionButton = ({
  errorCode,
  errorMessage,
}: ErrorFallbackSubscriptionButtonProps) => {
  const themeContext = useThemeContext();
  useErrorFallbackLogic({
    errorCode,
    errorMessage,
  });

  return (
    <Wrapper alignItems="center" gap="1rem">
      <UnderLineText
        fontSize={16}
        fontColor={themeContext.onSurface}
        fontWeight="bold"
        underLineColor={themeContext.primary}
      >
        알림 설정 불가
      </UnderLineText>
      <ToggleButton
        disabled={true}
        checked={false}
        handleChange={handleClickToggleButton}
      />
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)`
  padding: 0 1rem;
  margin: 1rem 0 0.5rem 0;
`;
