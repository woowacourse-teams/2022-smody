import styled, { css } from 'styled-components';

import { TextProps, FontSizeType } from 'components/@shared/Text/type';

export const Text = styled.span<TextProps>`
  ${({ size, color }) => css`
    font-size: ${fontSizeMapper[size ?? 16]}rem;
    color: ${color};
  `}
`;

const fontSizeMapper: Record<FontSizeType, number> = {
  10: 0.625,
  12: 0.75,
  14: 0.875,
  16: 1,
  20: 1.25,
  24: 1.5,
  32: 2,
  40: 2.5,
  48: 3,
};
