import { BottomSheetProps } from './type';
import ReactDOM from 'react-dom';
import styled, { css } from 'styled-components';

import { Z_INDEX } from 'constants/css';

export const BottomSheet = ({ children, handleCloseBottomSheet }: BottomSheetProps) => {
  return (
    <>
      {ReactDOM.createPortal(
        <Backdrop onClick={handleCloseBottomSheet} />,
        document.getElementById('backdrop-root') as HTMLElement,
      )}
      {ReactDOM.createPortal(
        <BottomSheetContent>{children}</BottomSheetContent>,
        document.getElementById('overlay-root') as HTMLElement,
      )}
    </>
  );
};

const BottomSheetContent = styled.div`
  ${({ theme }) => css`
    position: fixed;
    left: 0;
    bottom: 0;
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
    width: 100%;
    height: 220px;
    padding: 34px 0;
    border-radius: 5px 5px 0 0;
    background-color: ${theme.surface};
    z-index: ${Z_INDEX.MODAL};
    transition: bottom 0.4s linear;
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
