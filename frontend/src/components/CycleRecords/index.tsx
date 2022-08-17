import { useCycleRecords } from './useCycleRecords';
import styled from 'styled-components';

import { FlexBox } from 'components/@shared/FlexBox';

import { InfiniteScroll } from 'components/InfiniteScroll';
import { LoadingSpinner } from 'components/LoadingSpinner';
import { Record } from 'components/Record';

export const CycleRecords = () => {
  const {
    myCyclesInfiniteData,
    isFetchingMyCycles,
    hasNextPageMyCycles,
    fetchNextPageMyCycles,
  } = useCycleRecords();

  if (typeof myCyclesInfiniteData === 'undefined') {
    return null;
  }

  return (
    <InfiniteScroll
      loadMore={fetchNextPageMyCycles}
      hasMore={hasNextPageMyCycles}
      isFetching={isFetchingMyCycles}
      loader={<LoadingSpinner />}
    >
      <FlexBox as="ul" flexDirection="column" alignItems="center" gap="1rem">
        {myCyclesInfiniteData?.pages.map((page) =>
          page?.data.map((cycle) => (
            <RecordList key={cycle.cycleId}>
              <Record {...cycle} />
            </RecordList>
          )),
        )}
      </FlexBox>
    </InfiniteScroll>
  );
};

const RecordList = styled.li`
  width: 100%;
`;
