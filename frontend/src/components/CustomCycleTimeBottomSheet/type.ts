import { JoinChallengeProps } from 'hooks/usePostJoinChallenge';

export interface CustomCycleTimeBottomSheetProps {
  challengeName: string;
  startHour: number;
  joinChallenge: ({ challengeName, startTime }: JoinChallengeProps) => void;
  handleCloseBottomSheet: () => void;
}
