import { ModalOverlayProps } from './type';
import ReactDOM from 'react-dom';
import styled, { css } from 'styled-components';

export default function ModalOverlay({ children, handleCloseModal }: ModalOverlayProps) {
  return (
    <>
      {ReactDOM.createPortal(
        <Backdrop onClick={handleCloseModal} />,
        document.getElementById('backdrop-root') as HTMLElement,
      )}
      {ReactDOM.createPortal(
        <Modal>{children}</Modal>,
        document.getElementById('overlay-root') as HTMLElement,
      )}
    </>
  );
}

const Modal = styled.div`
  ${({ theme }) => css`
    height: 347px;
    width: 366px;
    padding: 25px;
    margin: 0;
    background-color: ${theme.surface};
    border-radius: 20px;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translateX(-50%) translateY(-50%);
    display: flex;
    justify-content: center;
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
  `}
`;
