import { CustomCycleTimeBottomSheetProps } from './type';

export const useCustomCycleTimeBottomSheet = ({
  challengeName,
  startHour,
  joinChallenge,
  handleCloseBottomSheet,
}: CustomCycleTimeBottomSheetProps) => {
  const handleJoinTodayChallenge = (selectHour?: number) => {
    if (selectHour === undefined) {
      joinChallenge({ challengeName });
      handleCloseBottomSheet();
      return;
    }

    const currentDate = new Date();
    currentDate.setHours(selectHour);
    currentDate.setMinutes(0);
    currentDate.setSeconds(0);
    if (selectHour < startHour) {
      currentDate.setDate(currentDate.getDate() + 1);
    }
    joinChallenge({ challengeName, startTime: currentDate });
    handleCloseBottomSheet();
  };

  return handleJoinTodayChallenge;
};
