import { AvailablePickedColor } from 'types/style';

export type ChallengeExplanationTextProps = {
  color: AvailablePickedColor;
};

export type ChallengeDetailProps = {
  challengeName: string;
  challengerCount: number;
  emojiIndex: number;
  colorIndex: number;
  description: string;
};
