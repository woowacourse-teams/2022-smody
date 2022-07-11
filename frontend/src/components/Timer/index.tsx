import { useContext, useEffect, useState } from 'react';
import { BiAlarm } from 'react-icons/bi';
import { ThemeContext } from 'styled-components';

import { FlexBox, Text } from 'components';
import { TimerProps } from 'components/Timer/type';

import { TIME_UPDATE_MS_PERIOD } from 'constants/domain';

const getLeftTimeMessage = (certEndDate: Date) => {
  const inMillisecond = 1000;
  const inMinutes = 60;
  const inHours = 3600;
  const inDays = 86400;

  const endTime = certEndDate.getTime();
  const nowTime = new Date().getTime();

  let remainSeconds = (endTime - nowTime) / inMillisecond;
  let closingMessage = '남았습니다';

  if (remainSeconds >= inDays) {
    closingMessage = '후부터 인증할 수 있습니다';
    remainSeconds -= inDays;
  }

  if (remainSeconds >= inHours) {
    return Math.floor(remainSeconds / inHours) + '시간 ' + closingMessage;
  }

  if (remainSeconds >= inMinutes) {
    return Math.floor(remainSeconds / inMinutes) + '분 ' + closingMessage;
  }
  return '1분 미만 ' + closingMessage;
};

export const Timer = ({ certEndDate }: TimerProps) => {
  const [message, setMessage] = useState(() => getLeftTimeMessage(certEndDate));
  const themeContext = useContext(ThemeContext);
  const timerColor = themeContext.onSurface;

  useEffect(() => {
    const intervalId = setInterval(() => {
      const newMessage = getLeftTimeMessage(certEndDate);
      if (newMessage !== message) {
        setMessage(newMessage);
      }
    }, TIME_UPDATE_MS_PERIOD);

    return () => clearInterval(intervalId);
  }, []);

  return (
    <FlexBox>
      <BiAlarm color={timerColor} />
      <Text color={timerColor} size={20}>
        {message}
      </Text>
    </FlexBox>
  );
};
