import styled, { css } from 'styled-components';

import { ButtonProps } from 'components/@shared/Button/type';

const SIZES = {
  small: { minWidth: '50px', height: '29px', fontSize: '12px' },
  medium: { minWidth: '149px', height: '34px', fontSize: '14px' },
  large: { minWidth: '100%', height: '45px', fontSize: '15px' },
};

export const Button = styled.button<ButtonProps>`
  ${({ theme, size, isActive = true }) => css`
    height: ${SIZES[size].height};
    min-width: ${SIZES[size].minWidth};
    width: fit-content;
    vertical-align: middle;
    text-align: center;
    padding: 0rem 1rem;
    background-color: transparent;
    border: none;
    cursor: pointer;
    transition: 0.3s;

    border-radius: 7px;
    ${isActive
      ? css`
          background-color: ${theme.primary};
          color: ${theme.onPrimary};
          &:hover {
            filter: brightness(1.2);
          }
        `
      : css`
          background-color: ${theme.secondary};
          color: ${theme.onSecondary};
          &:hover {
            filter: brightness(0.9);
          }
        `}

    font-size: ${SIZES[size].fontSize};

    &:disabled {
      background-color: ${theme.disabled};
      cursor: default;
      &:hover {
        filter: brightness(1);
      }
    }
  `}
`;
