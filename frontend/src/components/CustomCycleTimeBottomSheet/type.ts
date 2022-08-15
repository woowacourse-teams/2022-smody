import { JoinChallengeProps } from 'hooks/usePostJoinChallenge';

export interface CustomCycleTimeBottomSheetProps {
  challengeName: string;
  joinChallenge: ({ challengeName, startTime }: JoinChallengeProps) => void;
  handleCloseBottomSheet: () => void;
}
