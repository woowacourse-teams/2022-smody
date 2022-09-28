import styled, { css } from 'styled-components';

import { TextProps } from 'components/@shared/Text/type';

import { fontSizeMapper } from 'constants/style';

export const Text = styled.p<TextProps>`
  ${({ size, color, fontWeight }) => css`
    font-size: ${fontSizeMapper[size ?? 16]}rem;
    color: ${color};
    font-weight: ${fontWeight};
    line-height: 1.5;
  `}
`;
