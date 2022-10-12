import { TooltipProps } from './type';
import { PropsWithChildren } from 'react';
import { AiOutlineCloseCircle } from 'react-icons/ai';
import { BsQuestionCircleFill } from 'react-icons/bs';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

export const Tooltip = ({
  children,
  title,
  isOpenTooltip,
  toggleTooltip,
  xPosition = 'middle',
  yPosition = 'bottom',
}: PropsWithChildren<TooltipProps>) => {
  const themeContext = useThemeContext();

  return (
    <Wrapper>
      <TooltipButton
        onClick={toggleTooltip}
        type="button"
        aria-expanded={isOpenTooltip}
        aria-label={title}
        aria-labelledby="tooltip-label"
      >
        {children ?? <BsQuestionCircleFill color={themeContext.primary} size={27} />}
      </TooltipButton>
      <HelpToggleMessage
        role="dialog"
        hidden={!isOpenTooltip}
        xPosition={xPosition}
        yPosition={yPosition}
      >
        <span role="tooltip" id="tooltip-label">
          {title}
        </span>
        <HelpToggleCloseButton type="button" onClick={toggleTooltip} aria-label="닫기">
          <AiOutlineCloseCircle color={themeContext.primary} size={20} />
        </HelpToggleCloseButton>
      </HelpToggleMessage>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  position: relative;
`;

const TooltipButton = styled.button``;

const HelpToggleMessage = styled.div<{ xPosition: string; yPosition: string }>`
  ${({ theme, xPosition, yPosition }) => css`
    position: absolute;
    width: 300px;
    padding: 10px 26px 10px 10px;
    font-size: 14px;
    line-height: 1.3;
    border-radius: 10px;
    border: 1px solid ${theme.primary};
    background-color: ${theme.surface}
    color: ${theme.onSurface};
    top:  ${yPosition === 'top' ? '-43px' : '32px'};
    left: ${xPosition === 'middle' ? '-150px' : xPosition === 'left' ? '-265px' : '0'};
  `}
`;

const HelpToggleCloseButton = styled.button`
  position: absolute;
  top: 0;
  right: 0;
  padding: 8px;
  text-align: end;
`;
