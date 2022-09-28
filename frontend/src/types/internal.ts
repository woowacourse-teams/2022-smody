import { PickType } from 'smody-library';

export type ErrorResponse = {
  code: number;
  message: string;
};

export type ErrorType = {
  errorCode: PickType<ErrorResponse, 'code'> | null;
  errorMessage: PickType<ErrorResponse, 'message'> | null;
};

type ValidatorReturns = {
  isValidated: boolean;
  message: string;
};

export type ValidatorFunction = (
  value: string,
  optionalValue?: string,
) => ValidatorReturns;
