export interface ChallengeItemProps {
  challengeId: number;
  challengeName: string;
  challengerCount: number;
  isInProgress: boolean;
}

export type useChallengeItemProps = Pick<
  ChallengeItemProps,
  'challengeId' | 'challengeListRefetch'
>;
