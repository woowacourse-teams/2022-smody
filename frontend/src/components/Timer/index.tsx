import styled from 'styled-components';

import { FlexBox, Text } from 'components';
import { TimerProps } from 'components/Timer/type';
import { useTimer } from 'components/Timer/useTimer';

export const Timer = ({ certEndDate }: TimerProps) => {
  const { message, messageColor, time, timeColor } = useTimer(certEndDate);

  return (
    <Wrapper>
      <Text color={messageColor} size={16}>
        {message}
      </Text>
      <Text color={timeColor} fontWeight="bold" size={16}>
        {time}
      </Text>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  gap: '4px',
})``;
