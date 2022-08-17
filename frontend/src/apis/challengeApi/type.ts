import { Challenge, UserChallenge } from 'commonType';

export interface GetChallengeProps {
  searchValue: string;
}

export interface GetChallengeResponse {
  challengeId: number;
  challengeName: string;
  challengerCount: number;
  isInProgress: boolean;
  emojiIndex: number;
  colorIndex: number;
}

export interface GetMyChallengesProps {
  cursorId: number;
}

export interface GetMyChallengesResponse extends Challenge {
  emojiIndex: number;
  colorIndex: number;
}

export type GetChallengeByIdProps = Pick<Challenge, 'challengeId'>;

export interface GetChallengeByIdResponse
  extends Pick<
    UserChallenge,
    'challengeId' | 'challengeName' | 'challengerCount' | 'isInProgress'
  > {
  emojiIndex: number;
  colorIndex: number;
  description: string;
}

export type GetChallengersByIdProps = Pick<Challenge, 'challengeId'>;

export interface GetChallengersByIdResponse {
  memberId: number;
  nickName: string;
  progressCount: number;
  picture: string;
  introduction: string;
}

export interface PostChallengeProps {
  challengeName: string;
  description: string;
  emojiIndex: number;
  colorIndex: number;
}

export type GetMyChallengeByIdProps = Pick<Challenge, 'challengeId'>;

export interface GetMyChallengeByIdResponse {
  challengeName: string;
  description: string;
  emojiIndex: number;
  colorIndex: number;
  successCount: number;
  cycleDetailCount: number;
}
