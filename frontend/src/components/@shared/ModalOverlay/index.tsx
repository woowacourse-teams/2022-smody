import { ModalOverlayProps, ModalProps } from './type';
import { PropsWithChildren } from 'react';
import ReactDOM from 'react-dom';
import styled, { css } from 'styled-components';

import { Z_INDEX } from 'constants/css';

export const ModalOverlay = ({
  children,
  handleCloseModal,
  isFullSize = false,
}: PropsWithChildren<ModalOverlayProps>) => {
  return (
    <>
      {ReactDOM.createPortal(
        <Backdrop onClick={handleCloseModal} isFullSize={isFullSize} />,
        document.getElementById('backdrop-root') as HTMLElement,
      )}
      {ReactDOM.createPortal(
        <Modal isFullSize={isFullSize} role="dialog" aria-modal="true">
          {children}
        </Modal>,
        document.getElementById('overlay-root') as HTMLElement,
      )}
    </>
  );
};

const Modal = styled.div<ModalProps>`
  ${({ theme, isFullSize }) => css`
    ${isFullSize
      ? css`
          max-height: 90vh;
          max-width: 90vw;
          background-color: transparent;
          backdrop-filter: blur(3px);
        `
      : css`
          min-height: 347px;
          width: 366px;
          max-width: 90vw;
          background-color: ${theme.surface};
        `}

    position: fixed;
    top: 50%;
    left: 50%;
    transform: translateX(-50%) translateY(-50%);
    margin: 0;
    border-radius: 20px;
    display: flex;
    justify-content: center;
    z-index: ${Z_INDEX.MODAL};
  `}
`;

const Backdrop = styled.div<ModalProps>`
  ${({ theme, isFullSize }) => css`
    ${isFullSize &&
    css`
      backdrop-filter: blur(3px);
    `}

    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100vh;
    background-color: ${theme.backdrop};
    z-index: ${Z_INDEX.MODAL};
  `}
`;
