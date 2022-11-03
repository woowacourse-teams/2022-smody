import styled, { keyframes } from 'styled-components';

import { FlexBox } from 'components/';

export const LoadingSpinner = () => {
  return (
    <FlexBox flexDirection="column" justifyContent="center" alignItems="center">
      <Spinner />
    </FlexBox>
  );
};

const spin = keyframes`
  to { -webkit-transform: rotate(360deg); }
`;

const Spinner = styled.div`
  display: inline-block;
  width: 50px;
  height: 50px;
  border-top: 3px solid ${({ theme }) => theme.primary};
  border-radius: 50%;
  animation: ${spin} 1s ease-in-out infinite;
  -webkit-animation: ${spin} 1s ease-in-out infinite;
`;
