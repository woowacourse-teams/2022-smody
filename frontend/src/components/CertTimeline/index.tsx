import { CertTimelineProps } from './type';
import styled, { css } from 'styled-components';

import { FlexBox } from 'components/@shared/FlexBox';

import { CertTimelineItem } from 'components/CertTimelineItem';

export const CertTimeline = ({ cyclePages }: CertTimelineProps) => {
  return (
    <Wrapper flexDirection="column" alignItems="flex-start">
      {cyclePages.map((cycleInfo) => (
        <CertTimelineItem key={cycleInfo.cycleId} cycleInfo={cycleInfo} />
      ))}
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)`
  ${({ theme }) => css`
    width: 100%;
    max-width: 800px;
    min-width: 390px;
    padding: 0;
    border-radius: 5px;
    border: 1px solid ${theme.primary};
    background-color: ${theme.surface};
  `}
`;
