import styled, { css } from 'styled-components';

import { ButtonProps } from 'components/Button/type';

const SIZES = {
  small: { minWidth: '', height: '1.5rem', fontSize: '0.8rem' },
  medium: { minWidth: '8rem', height: '2rem', fontSize: '1rem' },
  large: { minWidth: '100%', height: '3rem', fontSize: '1.2rem' },
};

export const Button = styled.button<ButtonProps>`
  ${({ theme, size }) => css`
  height: ${SIZES[size].height};
    min-width: ${SIZES[size].minWidth};
    width: 'fit-content'
    vertical-align: middle;
    text-align: center;
    padding: 0rem 1rem;
    background-color: transparent;
    border: none;
    cursor: pointer;

    border-radius: 1rem;
    background-color: ${theme.primary};
    color: ${theme.onPrimary};
    font-size: ${SIZES[size].fontSize};

    &:disabled {
      background-color: ${theme.disabled};
      cursor: default;
    }
  `}
`;
