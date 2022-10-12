import { TooltipProps } from './type';
import useTooltip from './useTooltip';
import { PropsWithChildren } from 'react';
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
}: PropsWithChildren<TooltipProps>) => {
  const themeContext = useThemeContext();
  const { isOpenTooltip, openTooltip, closeTooltip, closeTooltipOnBg } = useTooltip();

  return (
    <Wrapper isOpenTooltip={isOpenTooltip} onClick={closeTooltipOnBg}>
      <TooltipButton
        onClick={openTooltip}
        type="button"
        aria-expanded={isOpenTooltip}
        aria-label={ariaLabel}
        aria-labelledby="tooltip-label"
      >
        {icon ?? <BsQuestionCircleFill color={themeContext.onInput} size={24} />}
      </TooltipButton>
      <HelpToggleMessage
        role="dialog"
        hidden={!isOpenTooltip}
        xPosition={xPosition}
        yPosition={yPosition}
      >
        <span role="tooltip" id="tooltip-label">
          {children}
        </span>
        <HelpToggleCloseButton type="button" onClick={closeTooltip} aria-label="닫기">
          <AiOutlineCloseCircle color={themeContext.onInput} size={20} />
        </HelpToggleCloseButton>
      </HelpToggleMessage>
    </Wrapper>
  );
};

const Wrapper = styled.div<{ isOpenTooltip: boolean }>`
  position: relative;

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

const TooltipButton = styled.button``;

const HelpToggleMessage = styled.div<{ xPosition: string; yPosition: string }>`
  ${({ theme, xPosition, yPosition }) => css`
    z-index: ${Z_INDEX.CSS_MODAL};
    position: absolute;
    width: 300px;
    min-height: 40px;
    padding: 10px 26px 10px 10px;
    font-size: 14px;
    line-height: 1.3;
    border-radius: 10px;
    border: 1px solid ${theme.onInput};
    background-color: ${theme.surface};
    color: ${theme.onSurface};
    top: ${yPosition === 'top' ? '-43px' : '38px'};
    left: ${xPosition === 'middle' ? '-150px' : xPosition === 'left' ? '-265px' : '0'};

    // 드롭다운 메뉴 우측 상단 삼각형 팁 디자인
    &::after {
      top: -14px;
      right: 10px;
      right: ${xPosition === 'middle'
        ? '150px'
        : xPosition === 'left'
        ? '10px'
        : '273px'};
      content: '';
      border: 7px solid transparent;
      border-bottom-color: ${theme.surface};
      position: absolute;
    }

    &::before {
      top: -16px;
      right: ${xPosition === 'middle' ? '149px' : xPosition === 'left' ? '9px' : '272px'};
      content: '';
      border: 8px solid transparent;
      border-bottom-color: ${theme.onInput};
      position: absolute;
    }
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
