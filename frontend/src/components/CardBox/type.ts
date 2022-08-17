import { Challenge } from 'commonType';

export interface CardBoxProps extends Challenge {
  emojiIndex: number;
  colorIndex: number;
}

export type WrapperProps = { bgColor: string };

export type UseCardBoxProps = Pick<Challenge, 'challengeId'>;
