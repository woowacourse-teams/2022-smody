import { useContext, useState, useEffect } from 'react';
import { ThemeContext } from 'styled-components';

import { TIME_UPDATE_MS_PERIOD } from 'constants/domain';

const inMillisecond = 1000;
const inHours = 3600;
const inDays = 86400;

export const useTimer = (certEndDate: Date) => {
  const themeContext = useContext(ThemeContext);

  const getTimerInfo = () => {
    const endTime = certEndDate.getTime();
    const nowTime = new Date().getTime();

    let remainSeconds = (endTime - nowTime) / inMillisecond;
    let isValid = true;
    let message = '오늘 인증 마감까지 남은 시간';
    let color = themeContext.primary;

    if (remainSeconds >= inDays) {
      isValid = false;
      remainSeconds -= inDays;
      message = '다음 인증 시작까지 남은 시간';
    }

    const date = new Date(0);
    date.setSeconds(remainSeconds);
    const time = date.toISOString().substr(11, 5);

    if (remainSeconds < inHours) {
      color = themeContext.error;
    }

    return {
      message,
      messageColor: themeContext.disabled,
      time,
      timeColor: isValid ? color : themeContext.disabled,
    };
  };

  const [timerInfo, setTimerInfo] = useState(() => getTimerInfo());

  useEffect(() => {
    const intervalId = setInterval(() => {
      setTimerInfo(getTimerInfo());
    }, TIME_UPDATE_MS_PERIOD);

    return () => clearInterval(intervalId);
  }, []);

  useEffect(() => {
    setTimerInfo(getTimerInfo());
  }, [certEndDate]);

  return timerInfo;
};
