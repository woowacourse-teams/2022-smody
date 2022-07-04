import styled, { css } from 'styled-components';

import { FlexBoxProps } from 'components/@shared/FlexBox/types';

export const FlexBox = styled.div<FlexBoxProps>`
  display: flex;
  ${({ flexDirection, justifyContent, alignItems, gap }) => css`
    flex-direction: ${flexDirection};
    justify-content: ${justifyContent};
    align-items: ${alignItems};
    gap: ${gap};
  `}
`;
