interface ValidatorReturns {
  isValidated: boolean;
  message: string;
}

export type ValidatorFunction = (
  value: string,
  optionalValue?: string,
) => ValidatorReturns;

export type MappedKeyToUnion<T> = T extends { [key: string]: infer K } ? K : never;

// 챌린지 단건 조회 응답 타입
export interface Challenge {
  challengeId: number;
  challengeName: string;
  successCount: number;
}
// 사이클 단건 조회 응답 타입
export interface Cycle extends Challenge {
  cycleId: number;
  progressCount: number;
  startTime: string;
}
