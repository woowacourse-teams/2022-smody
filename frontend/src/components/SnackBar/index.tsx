import ReactDOM from 'react-dom';
import styled, { css } from 'styled-components';

import { Text, LinkText, FlexBox } from 'components';
import { SnackBarProps } from 'components/SnackBar/type';

export const SnackBar = ({ status, message, linkText, linkTo }: SnackBarProps) => {
  return (
    <>
      {ReactDOM.createPortal(
        <SnackBarElement status={status}>
          <Wrapper>
            <Text size={12}>{message}</Text>
            {linkTo && (
              <LinkText to={linkTo} fontWeight="bold" size={12}>
                {linkText}
              </LinkText>
            )}
          </Wrapper>
        </SnackBarElement>,
        document.getElementById('snackbar-root') as HTMLElement,
      )}
    </>
  );
};

const Wrapper = styled(FlexBox).attrs({
  direction: 'row',
  alignItems: 'center',
  justifyContent: 'space-between',
})``;

const SnackBarElement = styled.div<{ status: string }>`
  ${({ theme, status }) => css`
    width: 95%;
    max-width: 900px;
    margin: 0;
    text-align: center;
    padding: 1rem;
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    bottom: 4.8rem;
    font-size: 0.8rem;
    border-radius: 5px;
    color: ${status === 'SUCCESS' ? theme.onSuccess : theme.onError};
    background-color: ${status === 'SUCCESS' ? theme.success : theme.error};

    -webkit-animation: fade 0.5s; /* Safari, Chrome and Opera > 12.1 */
    -moz-animation: fade 0.5s; /* Firefox < 16 */
    -ms-animation: fade 0.5s; /* Internet Explorer */
    -o-animation: fade 0.5s; /* Opera < 12.1 */
    animation: fade 0.5s;

    @keyframes fade {
      from {
        opacity: 0;
      }
      to {
        opacity: 1;
      }
    }

    /* Firefox < 16 */
    @-moz-keyframes fade {
      from {
        opacity: 0;
      }
      to {
        opacity: 1;
      }
    }

    /* Safari, Chrome and Opera > 12.1 */
    @-webkit-keyframes fade {
      from {
        opacity: 0;
      }
      to {
        opacity: 1;
      }
    }

    /* Internet Explorer */
    @-ms-keyframes fade {
      from {
        opacity: 0;
      }
      to {
        opacity: 1;
      }
    }

    /* Opera < 12.1 */
    @-o-keyframes fade {
      from {
        opacity: 0;
      }
      to {
        opacity: 1;
      }
    }

    @keyframes fadeout {
      from {
        opacity: 1;
      }
      to {
        opacity: 0;
      }
    }

    /* Firefox < 16 */
    @-moz-keyframes fadeout {
      from {
        opacity: 1;
      }
      to {
        opacity: 0;
      }
    }

    /* Safari, Chrome and Opera > 12.1 */
    @-webkit-keyframes fadeout {
      from {
        opacity: 1;
      }
      to {
        opacity: 0;
      }
    }

    /* Internet Explorer */
    @-ms-keyframes fadeout {
      from {
        opacity: 1;
      }
      to {
        opacity: 0;
      }
    }

    /* Opera < 12.1 */
    @-o-keyframes fadeout {
      from {
        opacity: 1;
      }
      to {
        opacity: 0;
      }
    }
  `}
`;
