import { CustomCycleTimeBottomSheetProps } from './type';

export const useCustomCycleTimeBottomSheet = ({
  challengeName,
  joinChallenge,
  handleCloseBottomSheet,
}: CustomCycleTimeBottomSheetProps) => {
  const startHour = new Date().getHours();

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

  return { startHour, handleJoinTodayChallenge };
};
