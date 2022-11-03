import { ReactNode } from 'react';
import { ErrorType } from 'types/internal';

export type ErrorBoundaryProps = {
  pathname: string;
  renderFallback: (renderFallbackParams: ErrorType) => ReactNode;
};

export type ErrorBoundaryState = ErrorType & {
  hasError: boolean;
};
