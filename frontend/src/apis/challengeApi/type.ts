import { Challenge } from 'commonType';

export interface GetChallengeResponse {
  challengeId: number;
  challengeName: string;
  challengerCount: number;
}

export type GetChallengeByIdProps = Pick<Challenge, 'challengeId'>;

export interface GetChallengeByIdResponse extends Omit<Challenge, 'successCount'> {
  challengerCount: number;
}
