import { BellProps } from './type';
import BellIcon from 'assets/bell_icon.svg';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { AvailablePickedColor } from 'styles/type';

export const Bell = ({ count }: BellProps) => {
  const themeContext = useThemeContext();

  return (
    <IconWrapper fill={themeContext.primary}>
      <BellIcon />
      {!!count && <BellCount>{count}</BellCount>}
    </IconWrapper>
  );
};

const IconWrapper = styled.div`
  ${({ fill }: { fill: AvailablePickedColor }) => css`
    cursor: pointer;

    & svg path {
      fill: ${fill};
      stroke: none;
    }
  `}
`;

const BellCount = styled.em`
  ${({ theme }) => css`
    position: absolute;
    top: 1px;
    right: -1px;
    width: 0.9rem;
    height: 0.9rem;
    border-radius: 50%;
    background-color: ${theme.error};
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 0.7rem;
    padding-top: 1px;
    color: #fff;
  `}
`;
