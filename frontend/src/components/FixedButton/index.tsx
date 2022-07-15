import styled from 'styled-components';

import { Button } from 'components';

export const FixedButton = styled(Button).attrs({
  size: 'large',
})`
  position: fixed;
  left: 50%;
  bottom: 0;
  transform: translateX(-50%);
  margin: 0 auto 6.25rem;
  box-sizing: border-box;
  min-width: 80%;
  z-index: 2;
`;
