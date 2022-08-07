export interface ChallengeItemProps {
  challengeId: number;
  challengeName: string;
  challengerCount: number;
  isInProgress: boolean;
  challengeListRefetch: () => void;
}

export type useChallengeItemProps = Pick<
  ChallengeItemProps,
  'challengeId' | 'challengeListRefetch'
>;
