import { ChangedNumberItemProps } from './type';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';

import COLOR from 'styles/color';

export const ChangedNumberItem = ({
  from,
  to,
  unit,
  isReverse = false,
}: ChangedNumberItemProps) => {
  const themeContext = useThemeContext();
  const diff = isReverse ? from - to : to - from;
  const isPositiveNumber = diff >= 0;

  return (
    <Wrapper
      flexDirection="column"
      justifyContent="center"
      alignItems="center"
      gap="0.4rem"
    >
      <Text
        size={16}
        color={isPositiveNumber ? COLOR.RED : COLOR.BLUE}
        fontWeight="normal"
      >
        {isPositiveNumber ? '+' : '-'}
        {diff}
      </Text>
      <Text size={20} color={themeContext.onSurface} fontWeight="bold">
        {to}
        {unit}
      </Text>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)`
  width: 90px;
  height: 80px;
  margin: 0 24px;
  padding-top: 10px;
`;
