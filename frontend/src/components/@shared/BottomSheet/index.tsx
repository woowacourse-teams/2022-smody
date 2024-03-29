import { BottomSheetContentProps, BottomSheetProps } from './type';
import { PropsWithChildren } from 'react';
import ReactDOM from 'react-dom';
import styled, { css, keyframes } from 'styled-components';

import { Z_INDEX } from 'constants/css';

export const BottomSheet = ({
  children,
  height,
  handleCloseBottomSheet,
}: PropsWithChildren<BottomSheetProps>) => {
  return (
    <>
      {ReactDOM.createPortal(
        <Backdrop onClick={handleCloseBottomSheet} />,
        document.getElementById('backdrop-root') as HTMLElement,
      )}
      {ReactDOM.createPortal(
        <BottomSheetContent height={height} role="dialog" aria-modal="true">
          {children}
        </BottomSheetContent>,
        document.getElementById('overlay-root') as HTMLElement,
      )}
    </>
  );
};

const slideUp = keyframes`
  from{
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
`;

const BottomSheetContent = styled.div<BottomSheetContentProps>`
  ${({ theme, height }) => css`
    position: fixed;
    left: 0;
    bottom: 0;
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
    width: 100%;
    ${height &&
    css`
      height: ${height};
    `}
    max-height: 100vh;
    overflow-y: auto;
    padding: 34px 0;
    border-radius: 5px 5px 0 0;
    background-color: ${theme.surface};
    z-index: ${Z_INDEX.MODAL};
    transition: bottom 0.4s linear;

    animation: 0.25s ease-out forwards ${slideUp};
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
