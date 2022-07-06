import { cycleData } from 'mocks/data';
import styled, { css } from 'styled-components';

import { FlexBox, CertItem } from 'components';

export const CertPage = () => {
  return (
    <Wrapper>
      {cycleData.map((cycle) => (
        <CertItem key={cycle.cycleId} {...cycle} />
      ))}
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  gap: '1rem',
  alignItems: 'center',
})``;
