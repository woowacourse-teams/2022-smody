import { useContext, useEffect, useState } from 'react';
import { BiAlarm } from 'react-icons/bi';
import { ThemeContext } from 'styled-components';

import { FlexBox, Text } from 'components';
import { TimerProps } from 'components/Timer/type';

const inMillisecond = 1000;
const inMinutes = 60;
const inHours = 3600;

export const Timer = ({ certEndDate }: TimerProps) => {
  const themeContext = useContext(ThemeContext);
  const [_, setToggle] = useState(false);

  useEffect(() => {
    const intervalId = setInterval(() => {
      setToggle((prev) => !prev);
    }, inMillisecond * inMinutes);

    return () => clearInterval(intervalId);
  }, []);

  const calculateRemainTime = (certEndDate: Date) => {
    const endTime = certEndDate.getTime();
    const nowTime = new Date().getTime();

    const remainSeconds = (endTime - nowTime) / 1000;

    if (remainSeconds >= inHours) {
      return Math.floor(remainSeconds / inHours) + '시간 남았습니다';
    }
    if (remainSeconds >= inMinutes) {
      return Math.floor(remainSeconds / inMinutes) + '분 남았습니다';
    }
    return '1분 미만 남았습니다.';
  };

  return (
    <FlexBox>
      <BiAlarm />
      <Text color={themeContext.onSurface} size={20}>
        {calculateRemainTime(certEndDate)}
      </Text>
    </FlexBox>
  );
};
