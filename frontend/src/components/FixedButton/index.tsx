import styled from 'styled-components';

import { Button } from 'components';

import { Z_INDEX } from 'constants/css';

export const FixedButton = styled(Button).attrs({
  size: 'large',
})`
  position: fixed;
  left: 50%;
  bottom: 0;
  transform: translateX(-50%);
  margin: 0 auto 4.8rem;
  box-sizing: border-box;
  z-index: ${Z_INDEX.FIXED_BUTTON};
  min-width: 80%;
`;
