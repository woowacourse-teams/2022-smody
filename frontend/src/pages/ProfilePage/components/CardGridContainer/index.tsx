import { CardBox } from '../CardBox';
import useCardGridContainer from './useCardGridContainer';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { Text, EmptyContent, LoadingSpinner, InfiniteScroll, FlexBox } from 'components';

import { CLIENT_PATH } from 'constants/path';

export const CardGridContainer = () => {
  const themeContext = useThemeContext();
  const { myChallengeInfiniteData, hasNextPage, fetchNextPage, isFetching } =
    useCardGridContainer();

  if (myChallengeInfiniteData?.pages[0].data.length === 0) {
    return (
      <EmptyContent
        title="아직 참가한 챌린지가 없습니다 :)"
        description="챌린지에 참가해보세요💪💪"
        linkText="챌린지 검색 페이지로 이동하기"
        linkTo={CLIENT_PATH.CHALLENGE_SEARCH}
      />
    );
  }

  return (
    <FlexBox flexDirection="column" gap="0.4rem">
      <Text fontWeight="bold" size={20} color={themeContext.onBackground}>
        참가한 챌린지
      </Text>
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
    </FlexBox>
  );
};

const Grid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(128px, max-content));
  grid-gap: 3px;
  width: 100%;
  min-width: 390px;
`;
