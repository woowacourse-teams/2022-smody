import { Challenge } from 'commonType';

export interface CardBoxProps extends Omit<Challenge, 'challengerCount'> {
  emojiIndex: number;
  colorIndex: number;
}

export type WrapperProps = { bgColor: string };

export type UseCardBoxProps = Pick<Challenge, 'challengeId'>;
