import { useGetMySuccessChallenges } from 'apis';
import { Challenge } from 'commonType';
import React, { useContext } from 'react';
import styled, { ThemeContext } from 'styled-components';

import { CardBox, Text, EmptyContent, LoadingSpinner } from 'components';

import { CLIENT_PATH } from 'constants/path';

export const CardGridContainer = () => {
  // TODO : 성공한 챌린지 GET API 연결
  const themeContext = useContext(ThemeContext);
  const { isLoading, data, hasNextPage, fetchNextPage } = useGetMySuccessChallenges();

  const loadMore = () => {
    if (hasNextPage) {
      fetchNextPage();
    }
  };

  if (isLoading || typeof data === 'undefined') {
    return <LoadingSpinner />;
  }

  if (data.pages[0].data.length === 0) {
    return (
      <EmptyContent
        title="아직 성공한 챌린지가 없습니다 :)"
        description="첫 챌린지를 힘내서 해보아요💪💪"
        linkText="인증 페이지로 이동하기"
        linkTo={CLIENT_PATH.CERT}
      />
    );
  }

  return (
    <div>
      <Text fontWeight="bold" size={20} color={themeContext.onBackground}>
        성공한 챌린지
      </Text>
      <Line />
      <Grid>
        {data.pages.map((page) => {
          if (typeof page === 'undefined' || typeof page.data === 'undefined') {
            return null;
          }

          return page.data.map((challenge: Challenge) => (
            <CardBox
              key={challenge.challengeId}
              {...challenge}
              bgColor="rgb(254, 214, 214)"
              emoji="🎈"
            />
          ));
        })}
      </Grid>
      <button onClick={loadMore}>다음페이지</button>
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
