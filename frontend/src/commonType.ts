interface ValidatorReturns {
  isValidated: boolean;
  message: string;
}

export type ValidatorFunction = (
  value: string,
  optionalValue?: string,
) => ValidatorReturns;

export type MappedKeyToUnion<T> = T extends { [key: string]: infer K } ? K : never;

export interface Cycle {
  cycleId: number;
  challengeId: number;
  challengeName: string;
  progressCount: number;
  startTime: string;
  successCount: number;
}
