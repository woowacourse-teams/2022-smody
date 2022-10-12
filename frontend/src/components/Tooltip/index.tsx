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
      <HelpToggleMessage role="dialog" hidden={!isOpenTooltip}>
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
  top: 50%;
`;

const TooltipButton = styled.button``;

const HelpToggleMessage = styled.div`
  ${({ theme }) => css`
    position: absolute;
    width: 300px;
    top: 30px;
    left: -150px;
    padding: 16px 12px 12px;
    font-size: 14px;
    border-radius: 10px;
    border: 1px solid ${theme.primary};
    background-color: ${theme.surface}
    color: ${theme.onSurface};
  `}
`;

const HelpToggleCloseButton = styled.button`
  position: absolute;
  top: 0;
  right: 0;
  padding: 8px;
  text-align: end;
`;
