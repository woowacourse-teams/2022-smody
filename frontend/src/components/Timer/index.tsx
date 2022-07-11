import { BiAlarm } from 'react-icons/bi';

import { FlexBox, Text } from 'components';
import { TimerProps } from 'components/Timer/type';
import { useTimer } from 'components/Timer/useTimer';

export const Timer = ({ certEndDate }: TimerProps) => {
  const { message, color } = useTimer(certEndDate);

  return (
    <FlexBox>
      <BiAlarm color={color} />
      <Text color={color} size={20}>
        {message}
      </Text>
    </FlexBox>
  );
};
