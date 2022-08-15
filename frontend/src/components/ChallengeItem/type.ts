export interface ChallengeItemProps {
  challengeId: number;
  challengeName: string;
  challengerCount: number;
  isInProgress: boolean;
  emojiIndex: number;
  colorIndex: number;
}

export type useChallengeItemProps = Pick<
  ChallengeItemProps,
  'challengeId' | 'isInProgress'
>;
