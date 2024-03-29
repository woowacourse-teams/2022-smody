import { ErrorBoundaryProps, ErrorBoundaryState } from './type';
import { AxiosError } from 'axios';
import { PropsWithChildren } from 'react';
import React from 'react';

import { ERROR_MESSAGE } from 'constants/message';

const UNKNOWN_ERROR_MESSAGE = '알 수 없는 오류가 발생했습니다.';
const OFFLINE_ERROR_MESSAGE =
  '네트워크가 오프라인 상태입니다. 인증, 챌린지, 피드 페이지에서 마지막으로 조회한 데이터만 볼 수 있습니다.';

const INITIAL_STATE = { hasError: false, errorCode: null, errorMessage: null };

export class ErrorBoundary extends React.Component<
  PropsWithChildren<ErrorBoundaryProps>,
  ErrorBoundaryState
> {
  constructor(props: PropsWithChildren<ErrorBoundaryProps>) {
    super(props);

    this.state = INITIAL_STATE;
  }

  componentDidUpdate(prevProps: PropsWithChildren<ErrorBoundaryProps>) {
    const { pathname: currentPathname } = this.props;
    const { pathname: prevPathname } = prevProps;

    if (prevPathname !== currentPathname) {
      this.setState(INITIAL_STATE);
    }
  }

  static getDerivedStateFromError(error: Error) {
    if (!navigator.onLine) {
      return { hasError: true, errorCode: null, errorMessage: OFFLINE_ERROR_MESSAGE };
    }

    if (!(error instanceof AxiosError) || typeof error.response?.data === 'undefined') {
      return { hasError: true, errorCode: null, errorMessage: UNKNOWN_ERROR_MESSAGE };
    }

    const { code: errorCode } = error.response.data;
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
