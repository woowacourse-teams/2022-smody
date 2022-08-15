import { CustomCycleTimeBottomSheetProps } from './type';
import { ChangeEvent, useState } from 'react';

export const useCustomCycleTimeBottomSheet = ({
  challengeName,
  joinChallenge,
  handleCloseBottomSheet,
}: CustomCycleTimeBottomSheetProps) => {
  const startHour = new Date().getHours();
  const [selectTimeIndex, setSelectTimeIndex] = useState(startHour);

  const handleSelectTime = (event: ChangeEvent<HTMLInputElement>) => {
    setSelectTimeIndex(Number(event.target.value));
  };

  const handleJoinTodayChallenge = () => {
    const currentDate = new Date();
    currentDate.setHours(selectTimeIndex);
    currentDate.setMinutes(0);
    currentDate.setSeconds(0);
    if (selectTimeIndex < startHour) {
      currentDate.setDate(currentDate.getDate() + 1);
    }
    joinChallenge({ challengeName, startTime: currentDate });
    handleCloseBottomSheet();
  };

  return { startHour, selectTimeIndex, handleSelectTime, handleJoinTodayChallenge };
};
