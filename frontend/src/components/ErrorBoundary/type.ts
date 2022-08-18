import { ReactNode } from 'react';

type ErrorCode = number | null;
type ErrorMessage = string | null;

export interface RenderFallbackParamsInterface {
  errorCode: ErrorCode;
  errorMessage: string;
}

export interface ErrorBoundaryProps {
  pathname: string;
  children: ReactNode;
  renderFallback: (renderFallbackParams: RenderFallbackParamsInterface) => ReactNode;
}

export interface ErrorBoundaryState {
  hasError: boolean;
  errorCode: ErrorCode;
  errorMessage: ErrorMessage;
}
