import { ErrorFallbackMainProps } from './type';

import { useErrorFallbackLogic } from 'hooks/useErrorFallbackLogic';

import { EmptyContent } from 'components';

export const ErrorFallbackMain = ({
  errorCode,
  errorMessage,
}: ErrorFallbackMainProps) => {
  useErrorFallbackLogic({
    errorCode,
    errorMessage,
  });

  return <EmptyContent title="Error가 발생했습니다. ❗️" description={errorMessage} />;
};
