import styled, { css } from 'styled-components';

import { ButtonProps } from 'components/Button/type';

const SIZES = {
  small: { minWidth: '50px', height: '29px', fontSize: '12px' },
  medium: { minWidth: '149px', height: '34px', fontSize: '14px' },
  large: { minWidth: '100%', height: '45px', fontSize: '15px' },
};

export const Button = styled.button<ButtonProps>`
  ${({ theme, size }) => css`
    height: ${SIZES[size].height};
    min-width: ${SIZES[size].minWidth};
    width: fit-content;
    vertical-align: middle;
    text-align: center;
    padding: 0rem 1rem;
    background-color: transparent;
    border: none;
    cursor: pointer;

    border-radius: 7px;
    background-color: ${theme.primary};
    color: ${theme.onPrimary};
    font-size: ${SIZES[size].fontSize};

    &:disabled {
      background-color: ${theme.disabled};
      cursor: default;
    }
  `}
`;
