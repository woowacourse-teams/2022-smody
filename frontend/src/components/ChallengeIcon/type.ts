import { PickType } from 'smody-library';
import { Challenge } from 'types/challenge';

export type ChallengeIconProps = {
  size: 'small' | 'medium' | 'large';
  bgColor: string;
  emojiIndex: number;
  challengeId?: number;
};

export type ImgIconListIndexSignature = {
  [key: PickType<Challenge, 'challengeId'>]: string;
};
