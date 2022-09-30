import { PopoverProps } from './type';
import { PropsWithChildren } from 'react';
import ReactDOM from 'react-dom';
import styled, { css } from 'styled-components';

import { Z_INDEX } from 'constants/css';

export const Popover = ({
  children,
  handleClosePopover,
}: PropsWithChildren<PopoverProps>) => {
  return (
    <>
      {ReactDOM.createPortal(
        <Backdrop onClick={handleClosePopover} />,
        document.getElementById('backdrop-root') as HTMLElement,
      )}
      {ReactDOM.createPortal(
        <Modal>{children}</Modal>,
        document.getElementById('overlay-root') as HTMLElement,
      )}
    </>
  );
};

const Modal = styled.div`
  ${({ theme }) => css`
    min-height: 347px;
    width: 366px;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translateX(-50%) translateY(-50%);
    margin: 0;
    background-color: ${theme.surface};
    border-radius: 20px;
    display: flex;
    justify-content: center;
    z-index: ${Z_INDEX.MODAL};
  `}
`;

const Backdrop = styled.div`
  ${({ theme }) => css`
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100vh;
    background-color: ${theme.backdrop};
    z-index: ${Z_INDEX.MODAL};
  `}
`;
