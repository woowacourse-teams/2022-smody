// type ValidatorReturns = {
//   isValidated: boolean;
//   message: string;
// };

// export type ValidatorFunction = (
//   value: string,
//   optionalValue?: string,
// ) => ValidatorReturns;

// export type ErrorResponse = {
//   code: number;
//   message: string;
// };

export type User = {
  nickname: string;
  introduction: string;
  picture: string;
  email: string;
};

// 챌린지 단건 조회 응답 타입
export type Challenge = {
  challengeId: number;
  challengeName: string;
  successCount: number; // UserChallenge로 내릴 것
  challengerCount: number; // 살릴 것
};

// 사이클 단건 조회 응답 타입
export type Cycle = Challenge & {
  cycleId: number;
  progressCount: number;
  startTime: string;
};

export type CycleDetail = {
  progressImage: string;
  progressTime: string;
  description: string;
};

export type CycleDetailWithId = CycleDetail & {
  cycleDetailId: number;
};

export type UserChallenge = Challenge & {
  successCount: number;
  isInProgress: boolean;
};

export type UserStat = {
  totalCount: number;
  successCount: number;
};

export type Feed = Pick<User, 'picture' | 'nickname'> &
  Pick<Cycle, 'progressCount'> &
  CycleDetailWithId &
  Pick<Challenge, 'challengeId' | 'challengeName'> & {
    memberId: number;
    commentCount: number;
  };

export type Comment = Pick<User, 'picture' | 'nickname'> & {
  commentId: number;
  memberId: number;
  content: string;
  createdAt: string;
  isMyComment: boolean;
};
