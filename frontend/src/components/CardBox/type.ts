import { Challenge } from 'commonType';

export interface CardBoxProps extends Challenge {
  bgColor: string;
  emoji: string;
}

export type WrapperProps = { bgColor: string };
