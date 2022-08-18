import { ErrorFallbackNotificationMessageProps } from './type';
import { useErrorFallbackNotificationMessage } from './useErrorFallbackNotificationMessage';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';

export const ErrorFallbackNotificationMessage = ({
  errorCode,
  errorMessage,
}: ErrorFallbackNotificationMessageProps) => {
  const themeContext = useThemeContext();
  useErrorFallbackNotificationMessage({
    errorCode,
    errorMessage,
  });

  return (
    <FlexBox justifyContent="center">
      <Text size={16} color={themeContext.error}>
        알림 목록을 불러올 수 없습니다.
      </Text>
    </FlexBox>
  );
};
