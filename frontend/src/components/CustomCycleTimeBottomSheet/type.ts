import { JoinChallengeProps } from 'hooks/usePostJoinChallenge';

export type CustomCycleTimeBottomSheetProps = {
  challengeName: string;
  joinChallenge: ({ challengeName, startTime }: JoinChallengeProps) => void;
  handleCloseBottomSheet: () => void;
};
