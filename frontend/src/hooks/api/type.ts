import { Challenge } from 'commonType';

export interface PostJoinChallengeProps extends Pick<Challenge, 'challengeId'> {
  successCallback?: () => void;
}
