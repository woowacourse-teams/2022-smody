import { Challenge, UserChallenge } from 'commonType';

export interface GetChallengeResponse {
  challengeId: number;
  challengeName: string;
  challengerCount: number;
  isInProgress: boolean;
}

export interface GetChallengeByIdProps extends Pick<Challenge, 'challengeId'> {}

export interface GetChallengeByIdResponse
  extends Pick<
    UserChallenge,
    'challengeId' | 'challengeName' | 'challengerCount' | 'isInProgress'
  > {}
