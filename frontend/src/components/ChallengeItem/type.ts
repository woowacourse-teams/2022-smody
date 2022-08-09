export interface ChallengeItemProps {
  challengeId: number;
  challengeName: string;
  challengerCount: number;
  isInProgress: boolean;
  emoji: string;
}

export type useChallengeItemProps = Pick<
  ChallengeItemProps,
  'challengeId' | 'challengeName' | 'isInProgress'
>;
