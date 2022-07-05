interface ValidatorReturns {
  isValidated: boolean;
  message: string;
}

export type ValidatorFunction = (
  value: string,
  optionalValue?: string,
) => ValidatorReturns;

export type MappedKeyToUnion<T> = T extends { [key: string]: infer K } ? K : never;
