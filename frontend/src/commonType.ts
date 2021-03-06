interface ValidatorReturns {
  isValidated: boolean;
  message: string;
}

export type ValidatorFunction = (
  value: string,
  optionalValue?: string,
) => ValidatorReturns;

export interface ErrorResponse {
  code: number;
  message: string;
}

export type MappedKeyToUnion<T> = T extends { [key: string]: infer K } ? K : never;

export interface User {
  nickname: string;
  introduction: string;
  picture: string;
  email: string;
}

// 챌린지 단건 조회 응답 타입
export interface Challenge {
  challengeId: number;
  challengeName: string;
  successCount: number; // UserChallenge로 내릴 것
  // challengerCount: number;  // 살릴 것
}
// 사이클 단건 조회 응답 타입
export interface Cycle extends Challenge {
  cycleId: number;
  progressCount: number;
  startTime: string;
}

// TODO : type.ts 전체 리팩토링 필요
export interface UserChallenge extends Challenge {
  successCount: number;
}

export interface UserStat {
  totalCount: number;
  successCount: number;
}
