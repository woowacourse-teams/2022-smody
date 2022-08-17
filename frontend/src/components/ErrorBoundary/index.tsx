import { ErrorBoundaryProps, ErrorBoundaryState } from './type';
import { AxiosError } from 'axios';
import { ErrorResponse } from 'commonType';
import React from 'react';

import { ERROR_MESSAGE } from 'constants/message';

const UNKNOWN_ERROR_MESSAGE = '알 수 없는 오류가 발생했습니다.';
const isAxiosError = (error: Error) => error instanceof AxiosError;

export class ErrorBoundary extends React.Component<
  ErrorBoundaryProps,
  ErrorBoundaryState
> {
  constructor(props: ErrorBoundaryProps) {
    super(props);

    this.state = { hasError: false, errorCode: null, errorMessage: null };
  }

  static getDerivedStateFromError(error: Error) {
    if (!isAxiosError(error)) {
      const { message } = error;
      return { hasError: true, errorCode: null, errorMessage: message };
    }

    const { response } = error as AxiosError<ErrorResponse>;

    if (typeof response === 'undefined' || typeof response === 'undefined') {
      return { hasError: true, errorCode: null, errorMessage: UNKNOWN_ERROR_MESSAGE };
    }

    const { code: errorCode } = response.data;
    const errorMessage = ERROR_MESSAGE[errorCode] ?? UNKNOWN_ERROR_MESSAGE;

    return { hasError: true, errorCode, errorMessage };
  }

  render() {
    const { children, renderFallback } = this.props;
    const { hasError, errorCode, errorMessage } = this.state;

    if (hasError && errorMessage !== null) {
      return renderFallback({ errorCode, errorMessage });
    }

    return children;
  }
}
