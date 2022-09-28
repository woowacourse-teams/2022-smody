import styled, { css } from 'styled-components';

import { UnderLineTextProps } from 'components/UnderLineText/type';

import { Z_INDEX } from 'constants/css';
import { fontSizeMapper } from 'constants/style';

export const UnderLineText = styled.p<UnderLineTextProps>`
  ${({ fontSize, fontColor, fontWeight, underLineColor }) => css`
    position: relative;
    font-weight: ${fontWeight};
    display: inline-block;
    text-align: center;
    padding: 0 2px;
    color: ${fontColor};
    font-size: ${fontSizeMapper[fontSize ?? 16]}rem;
    z-index: ${Z_INDEX.UNDER_TEXT};

    &::after {
      content: '';
      display: block;
      position: absolute;
      left: 0;
      bottom: 1px;
      width: 100%;
      height: ${fontSizeMapper[fontSize ?? 16] * 0.4}rem;
      background-color: ${underLineColor};
      opacity: 0.5;
      z-index: ${Z_INDEX.UNDER_LINE};
    }
  `}
`;
