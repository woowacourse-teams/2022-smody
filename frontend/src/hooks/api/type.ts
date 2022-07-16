import { Challenge } from 'commonType';

export interface PostJoinChallengeProps extends Pick<Challenge, 'challengeId'> {
  isNavigator?: boolean;
  handleSuccessFunction?: () => void;
}
