import { useContext, useState, useEffect } from 'react';
import { ThemeContext } from 'styled-components';

import { TIME_UPDATE_MS_PERIOD } from 'constants/domain';

const inMillisecond = 1000;
const inMinutes = 60;
const inHours = 3600;
const inDays = 86400;

export const useTimer = (certEndDate: Date) => {
  const themeContext = useContext(ThemeContext);

  const getTimerInfo = () => {
    const endTime = certEndDate.getTime();
    const nowTime = new Date().getTime();

    let remainSeconds = (endTime - nowTime) / inMillisecond;
    let isValid = true;
    let message, color;

    if (remainSeconds >= inDays) {
      isValid = false;
      remainSeconds -= inDays;
    }

    switch (true) {
      case remainSeconds >= inHours:
        message = Math.floor(remainSeconds / inHours) + '시간 ';
        color = themeContext.success;
        break;
      case remainSeconds >= inMinutes:
        message = Math.floor(remainSeconds / inMinutes) + '분 ';
        color = themeContext.error;
        break;
      default:
        message = '1분 미만 ';
        color = themeContext.error;
    }

    return {
      message: message + (isValid ? '남았습니다' : '후부터 인증할 수 있습니다'),
      color: isValid ? color : themeContext.disabled,
    };
  };

  const [timerInfo, setTimerInfo] = useState(() => getTimerInfo());

  useEffect(() => {
    const intervalId = setInterval(() => {
      const newTimerInfo = getTimerInfo();
      if (timerInfo.message !== newTimerInfo.message) {
        setTimerInfo(newTimerInfo);
      }
    }, TIME_UPDATE_MS_PERIOD);

    return () => clearInterval(intervalId);
  }, []);

  useEffect(() => {
    const newTimerInfo = getTimerInfo();
    setTimerInfo(newTimerInfo);
  }, [certEndDate]);

  return timerInfo;
};
