import { Challenge, UserChallenge } from 'commonType';

export interface GetChallengeResponse {
  challengeId: number;
  challengeName: string;
  challengerCount: number;
  isInProgress: boolean;
  emoji: string;
}

export interface GetMySuccessChallengesResponse extends Challenge {
  emoji: string;
}

export type GetChallengeByIdProps = Pick<Challenge, 'challengeId'>;

export interface GetChallengeByIdResponse
  extends Pick<
    UserChallenge,
    'challengeId' | 'challengeName' | 'challengerCount' | 'isInProgress'
  > {
  emoji: string;
  description: string;
}
