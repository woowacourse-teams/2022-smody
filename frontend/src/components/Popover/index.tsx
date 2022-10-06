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
    height: 200px;
    overflow-y: scroll;
    padding: 0.5rem 0rem;
    width: 96%;
    position: fixed;
    bottom: 128px;
    left: 50%;
    transform: translateX(-50%);
    margin: 0;
    background-color: ${theme.surface};
    border: 1px solid ${theme.primary};

    border-radius: 20px 0 0 20px;
    display: flex;
    justify-content: center;
    z-index: ${Z_INDEX.MODAL};

    // 스크롤바
    /* 스크롤바 설정*/
    &::-webkit-scrollbar {
      width: 4px;
    }

    /* 스크롤바 막대 설정*/
    &::-webkit-scrollbar-thumb {
      height: 17%;
      background-color: ${theme.primary};
      border-radius: 100px;
    }

    /* 스크롤바 뒷 배경 설정*/
    &::-webkit-scrollbar-track {
      background-color: ${theme.surface};
      border-radius: 100px;
    }
  `}
`;

const Backdrop = styled.div`
  ${({ theme }) => css`
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100vh;
    z-index: ${Z_INDEX.MODAL};
  `}
`;
