export interface ChallengeItemProps {
  challengeId: number;
  challengeName: string;
  challengerCount: number;
  challengeListRefetch: () => void;
}
