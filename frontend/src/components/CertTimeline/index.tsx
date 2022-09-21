import { CertTimelineProps } from './type';
import styled, { css } from 'styled-components';

import { FlexBox } from 'components/@shared/FlexBox';

import { CertTimelineItem } from 'components/CertTimelineItem';

export const CertTimeline = ({ cyclePages }: CertTimelineProps) => {
  const itemTimeList = cyclePages.map((cycleInfo, index) => {
    const thisItemStartDate = new Date(cycleInfo.startTime);
    const nextItemStartDate = new Date(cyclePages[index + 1]?.startTime);

    const nowDate = new Date();

    if (nowDate < thisItemStartDate) {
      return 'future';
    }
    if (nowDate >= thisItemStartDate && nowDate < nextItemStartDate) {
      return 'now';
    }
    return 'past';
  });
  console.log('@@', itemTimeList);

  return (
    <Wrapper flexDirection="column" alignItems="flex-start">
      {cyclePages.map((cycleInfo, index) => (
        <CertTimelineItem
          key={cycleInfo.cycleId}
          cycleInfo={cycleInfo}
          time={itemTimeList[index]}
        />
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
