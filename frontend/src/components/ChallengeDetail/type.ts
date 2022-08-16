import { AvailablePickedColor } from 'styles/type';

export interface ChallengeExplanationTextProps {
  color: AvailablePickedColor;
}

export interface ChallengeDetailProps {
  challengeName: string;
  challengerCount: number;
  emojiIndex: number;
  colorIndex: number;
  description: string;
}
