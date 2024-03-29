import { TooltipProps } from './type';
import useTooltip from './useTooltip';
import { PropsWithChildren, ReactNode } from 'react';
import { AiOutlineCloseCircle } from 'react-icons/ai';
import { BsQuestionCircleFill } from 'react-icons/bs';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { Z_INDEX } from 'constants/css';

export const Tooltip = ({
  children,
  icon,
  ariaLabel,
  xPosition = 'middle',
  yPosition = 'bottom',
  xDelta,
  yDelta,
  line = 1,
}: PropsWithChildren<TooltipProps>) => {
  const themeContext = useThemeContext();
  const { isOpenTooltip, openTooltip, closeTooltip, closeTooltipOnBg } = useTooltip();
  return (
    <Wrapper isOpenTooltip={isOpenTooltip} onClick={closeTooltipOnBg}>
      <ButtonWrapper icon={icon}>
        <TooltipButton
          onClick={openTooltip}
          type="button"
          aria-expanded={isOpenTooltip}
          aria-label={ariaLabel}
          aria-labelledby="tooltip-label"
        >
          {icon ?? <BsQuestionCircleFill color={themeContext.primary} size={24} />}
        </TooltipButton>
      </ButtonWrapper>
      <HelpToggleMessage
        role="dialog"
        hidden={!isOpenTooltip}
        xPosition={xPosition}
        yPosition={yPosition}
        xDelta={xDelta}
        yDelta={yDelta}
        line={line}
      >
        <span role="tooltip" id="tooltip-label">
          {children}
        </span>
        <HelpToggleCloseButton type="button" onClick={closeTooltip} aria-label="닫기">
          <AiOutlineCloseCircle color={themeContext.primary} size={20} />
        </HelpToggleCloseButton>
      </HelpToggleMessage>
    </Wrapper>
  );
};

const Wrapper = styled.div<{ isOpenTooltip: boolean }>`
  position: relative;
  margin: 0;
  ${({ isOpenTooltip }) => isOpenTooltip && EntireBackground}
`;

const EntireBackground = css`
  &:before {
    z-index: ${Z_INDEX.CSS_MODAL_BG};
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    display: block;
    cursor: default;
    content: ' ';
    background: transparent;
  }
`;

const TooltipButton = styled.button`
  ${({ theme }) => css`
    color: ${theme.onPrimary};
    padding: 0;

    &:hover {
      filter: brightness(1.2);
    }
  `}
`;

const StringIconStyle = css`
  ${({ theme }) => css`
    background-color: ${theme.primary};
    border-radius: 7px;
    padding: 5px 6px;
  `}
`;

const ButtonWrapper = styled.div<{ icon: ReactNode }>`
  ${({ icon }) => icon && StringIconStyle}
`;

const HelpToggleMessage = styled.div<{
  xPosition: string;
  yPosition: string;
  xDelta?: string;
  yDelta?: string;
  line: number;
}>`
  ${({ theme, xPosition, yPosition, xDelta, yDelta, line }) => css`
    z-index: ${Z_INDEX.CSS_MODAL};
    position: absolute;
    width: 250px;
    min-height: 40px;
    padding: 10px 26px 10px 10px;
    font-size: 14px;
    line-height: 1.6;
    border-radius: 10px;
    border: 1px solid ${theme.primary};
    background-color: ${theme.surface};
    color: ${theme.onSurface};
    top: ${yDelta ? yDelta : yPosition === 'top' ? '-49px' : '38px'};
    left: ${xDelta
      ? xDelta
      : xPosition === 'middle'
      ? '-113px'
      : xPosition === 'left'
      ? '-219px'
      : '0'};

    // 드롭다운 메뉴 상단 삼각형 팁 디자인
    ${yPosition === 'bottom' &&
    css`
      &::after {
        top: -14px;
        right: ${xPosition === 'middle'
          ? '150px'
          : xPosition === 'left'
          ? '10px'
          : '223px'};
        content: '';
        border: 7px solid transparent;
        border-bottom-color: ${theme.surface};
        position: absolute;
      }

      &::before {
        top: -16px;
        right: ${xPosition === 'middle'
          ? '149px'
          : xPosition === 'left'
          ? '9px'
          : '222px'};
        content: '';
        border: 8px solid transparent;
        border-bottom-color: ${theme.primary};
        position: absolute;
      }
    `}

    // 드롭다운 메뉴 하단 삼각형 팁 디자인
    ${yPosition === 'top' &&
    css`
      &::after {
        top: ${37 * line}px;
        right: ${xPosition === 'middle'
          ? '150px'
          : xPosition === 'left'
          ? '10px'
          : '223px'};
        content: '';
        border: 7px solid transparent;
        border-top-color: ${theme.surface};
        position: absolute;
      }

      &::before {
        top: ${37 * line + 2}px;
        right: ${xPosition === 'middle'
          ? '149px'
          : xPosition === 'left'
          ? '9px'
          : '222px'};
        content: '';
        border: 8px solid transparent;
        border-top-color: ${theme.primary};
        position: absolute;
      }
    `}
  `}
`;

const HelpToggleCloseButton = styled.button`
  position: absolute;
  top: 0;
  right: 0;
  padding: 8px;
  text-align: end;
  cursor: pointer;
`;
