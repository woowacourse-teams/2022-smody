import useCardGridContainer from './useCardGridContainer';
import { GetMyChallengesResponse } from 'apis/challengeApi/type';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { CardBox, Text, EmptyContent, LoadingSpinner, InfiniteScroll } from 'components';

import { CLIENT_PATH } from 'constants/path';

export const CardGridContainer = () => {
  const themeContext = useThemeContext();
  const {
    myChallengeInfiniteData,
    hasNextPage,
    fetchNextPage,
    isFetching,
    isError,
    savedMyChallenges,
  } = useCardGridContainer();

  if (isError) {
    return (
      <div>
        <Text fontWeight="bold" size={20} color={themeContext.onBackground}>
          참가한 챌린지
        </Text>
        <Line />
        <Grid>
          {savedMyChallenges.map((challenge: GetMyChallengesResponse) => (
            <CardBox key={challenge.challengeId} {...challenge} />
          ))}
        </Grid>
      </div>
    );
  }

  if (myChallengeInfiniteData?.pages[0].data.length === 0) {
    return (
      <EmptyContent
        title="아직 참가한 챌린지가 없습니다 :)"
        description="챌린지에 참가해보세요💪💪"
        linkText="챌린지 검색 페이지로 이동하기"
        linkTo={CLIENT_PATH.SEARCH}
      />
    );
  }

  return (
    <div>
      <Text fontWeight="bold" size={20} color={themeContext.onBackground}>
        참가한 챌린지
      </Text>
      <Line />
      <InfiniteScroll
        loadMore={fetchNextPage}
        hasMore={hasNextPage}
        isFetching={isFetching}
        loader={<LoadingSpinner />}
      >
        <Grid>
          {myChallengeInfiniteData?.pages.map((page) =>
            page?.data?.map((challenge) => (
              <CardBox key={challenge.challengeId} {...challenge} />
            )),
          )}
        </Grid>
      </InfiniteScroll>
    </div>
  );
};

const Grid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(128px, max-content));
  grid-gap: 3px;
  width: 100%;
  min-width: 390px;
`;

const Line = styled.hr`
  border-style: solid none none;
  margin-bottom: 0;
  border-color: ${({ theme }) => theme.disabled};
`;
