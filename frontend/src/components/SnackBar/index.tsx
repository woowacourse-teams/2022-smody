import ReactDOM from 'react-dom';
import styled, { css } from 'styled-components';

import { SnackBarProps } from 'components/SnackBar/type';

export const SnackBar = ({ message, status }: SnackBarProps) => {
  return (
    <>
      {ReactDOM.createPortal(
        <SnackbarElement status={status}>{message}</SnackbarElement>,
        document.getElementById('snackbar-root') as HTMLElement,
      )}
    </>
  );
};

const SnackbarElement = styled.div<{ status: string }>`
  ${({ theme, status }) => css`
    min-width: 250px;
    margin: 0;
    text-align: center;
    padding: 1rem;
    position: fixed;
    left: 50%;
    transform: translateX(-50%);
    bottom: 4.8rem;
    font-size: 1rem;
    border-radius: 5px;
    color: ${status === 'SUCCESS' ? theme.onSuccess : theme.onError};
    background-color: ${status === 'SUCCESS' ? theme.success : theme.error};

    -webkit-animation: fadein 0.5s, fadeout 0.5s 2.5s;
    animation: fadein 0.5s, fadeout 0.5s 2.5s;

    @-webkit-keyframes fadein {
      from {
        bottom: 0;
        opacity: 0;
      }
      to {
        bottom: 4.8rem;
        opacity: 1;
      }
    }

    @keyframes fadein {
      from {
        bottom: 0;
        opacity: 0;
      }
      to {
        bottom: 4.8rem;
        opacity: 1;
      }
    }

    @-webkit-keyframes fadeout {
      from {
        bottom: 4.8rem;
        opacity: 1;
      }
      to {
        bottom: 0;
        opacity: 0;
      }
    }

    @keyframes fadeout {
      from {
        bottom: 4.8rem;
        opacity: 1;
      }
      to {
        bottom: 0;
        opacity: 0;
      }
    }
  `}
`;
