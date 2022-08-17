import { CycleRecordListProps } from './type';
import { useCycleRecordList } from './useCycleRecordList';
import { useCycleRecords } from './useCycleRecords';
import styled from 'styled-components';

import {
  Button,
  FlexBox,
  InfiniteScroll,
  LoadingSpinner,
  Record,
  EmptyContent,
} from 'components';

import { CLIENT_PATH } from 'constants/path';

export const CycleRecords = () => {
  const { selectOption, handleSelectButton } = useCycleRecords();

  return (
    <FlexBox flexDirection="column">
      <FilterButtonWrapper justifyContent="flex-end" gap="10px">
        <Button
          size="small"
          isActive={selectOption === 'all'}
          onClick={() => handleSelectButton('all')}
        >
          전체
        </Button>
        <Button
          size="small"
          isActive={selectOption === 'success'}
          onClick={() => handleSelectButton('success')}
        >
          성공
        </Button>
      </FilterButtonWrapper>
      <CycleRecordList selectOption={selectOption} />
    </FlexBox>
  );
};

const CycleRecordList = ({ selectOption }: CycleRecordListProps) => {
  const {
    challengeId,
    myCyclesInfiniteData,
    isFetchingMyCycles,
    hasNextPageMyCycles,
    fetchNextPageMyCycles,
  } = useCycleRecordList(selectOption);

  if (typeof myCyclesInfiniteData === 'undefined') {
    return null;
  }

  if (selectOption === 'success' && myCyclesInfiniteData.pages[0].data.length === 0) {
    return (
      <EmptyContent
        title="아직 성공하지 못한 챌린지입니다 :)"
        description="세 번만 힘내서 도전해보아요!!"
        linkText="현재 챌린지 도전하러 가기"
        linkTo={`${CLIENT_PATH.CHALLENGE_DETAIL}/${challengeId}`}
      />
    );
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

const FilterButtonWrapper = styled(FlexBox)`
  margin: 10px auto;
  width: 100%;
  max-width: 1000px;
`;

const RecordList = styled.li`
  width: 100%;
`;
