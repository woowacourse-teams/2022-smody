import { useGetMySuccessChallenges } from 'apis';
import { Challenge } from 'commonType';
import React, { useContext } from 'react';
import styled, { ThemeContext } from 'styled-components';

import { CardBox, Text } from 'components';

export const CardGridContainer = () => {
  // TODO : 성공한 챌린지 GET API 연결
  const themeContext = useContext(ThemeContext);
  const { isLoading, data, hasNextPage, fetchNextPage } = useGetMySuccessChallenges();
  console.log('data', data);
  const loadMore = () => {
    if (hasNextPage) {
      fetchNextPage();
    }
  };

  if (isLoading || typeof data === 'undefined') {
    return <span>Loading...</span>;
  }

  return (
    <div>
      <Text fontWeight="bold" size={24} color={themeContext.onBackground}>
        성공한 챌린지
      </Text>
      <Line />
      <Grid>
        {data.pages.map((page) => {
          if (typeof page === 'undefined' || typeof page.data === 'undefined') {
            return null;
          }

          page.data.map((challenge: Challenge) => (
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
