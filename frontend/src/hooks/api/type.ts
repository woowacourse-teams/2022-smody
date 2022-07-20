import { Challenge } from 'commonType';

export interface PostJoinChallengeProps extends Pick<Challenge, 'challengeId'> {
  challengeName?: string;
  challengeListRefetch?: () => void;
}
