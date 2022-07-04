import styled, { css } from 'styled-components';

import { ButtonProps } from 'components/Button/type';

const SIZES = {
  small: { width: '439px', height: '32px' },
  medium: { width: '439px', height: '40px' },
  large: { width: '100%', height: '48px' },
};

export const Button = styled.button<ButtonProps>`
  ${({ theme, size }) => css`
    width: ${SIZES[size].width};
    height: ${SIZES[size].height};

    text-align: center;
    padding: 10px;
    background-color: transparent;
    border: none;
    cursor: pointer;

    border-radius: 5px;
    background-color: ${theme.primary};
    color: ${theme.onPrimary};
    font-size: 16px;

    &:disabled {
      background-color: ${theme.disabled};
      cursor: default;
    }
  `}
`;
