import styled, { css } from 'styled-components';

import { TextProps } from 'components/@shared/Text/type';

import { FontSizeType } from 'styles/type';

export const Text = styled.p<TextProps>`
  ${({ size, color, fontWeight }) => css`
    font-size: ${fontSizeMapper[size ?? 16]}rem;
    color: ${color};
    font-weight: ${fontWeight};
    line-height: 1.5;
  `}
`;

const fontSizeMapper: Record<FontSizeType, number> = {
  10: 0.625,
  11: 0.6875,
  12: 0.75,
  14: 0.875,
  16: 1,
  20: 1.25,
  24: 1.5,
  32: 2,
  40: 2.5,
  48: 3,
  70: 4.375,
};
