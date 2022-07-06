import styled, { css } from 'styled-components';

import { UnderLineTextProps } from 'components/UnderLineText/type';

import { FontSizeType } from 'styles/type';

export const UnderLineText = styled.p<UnderLineTextProps>`
  ${({ fontSize, fontColor, fontWeight, underLineColor }) => css`
    position: relative;
    font-weight: ${fontWeight};
    display: inline-block;
    text-align: center;
    padding: 0 2px;
    color: ${fontColor};
    font-size: ${fontSizeMapper[fontSize ?? 16]}rem;
    z-index: 0;

    &::after {
      content: '';
      display: block;
      position: absolute;
      left: 0;
      bottom: 0;
      width: 100%;
      height: ${fontSizeMapper[fontSize ?? 16] * 0.4}rem;
      background-color: ${underLineColor};
      opacity: 0.5;
      z-index: -1;
    }
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
