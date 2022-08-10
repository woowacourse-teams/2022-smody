import { BellProps } from './type';
import BellEmptyIcon from 'assets/bell_empty_icon.svg';
import BellFilledIcon from 'assets/bell_filled_icon.svg';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { AvailablePickedColor } from 'styles/type';

export const Bell = ({ count, isSubscribed }: BellProps) => {
  const themeContext = useThemeContext();

  return (
    <IconWrapper fill={themeContext.primary}>
      {isSubscribed ? <BellFilledIcon /> : <BellEmptyIcon />}
      {!!count && <BellCount>{count}</BellCount>}
    </IconWrapper>
  );
};

const IconWrapper = styled.div`
  ${({ fill }: { fill: AvailablePickedColor }) => css`
    cursor: pointer;

    & svg path {
      fill: ${fill};
    }
  `}
`;

const BellCount = styled.em`
  display: flex;
  justify-content: center;
  align-self: center;
  position: absolute;
  top: -2px;
  right: -7px;
  width: 1.3rem;
  height: 1.3rem;
  font-size: 0.9rem;
  padding-top: 2px;
  border-radius: 50%;
  background-color: red;
  color: white;
`;
