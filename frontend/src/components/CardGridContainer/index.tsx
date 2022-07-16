import { mySuccessChallenges } from 'mocks/data';
import { useContext } from 'react';
import styled, { ThemeContext } from 'styled-components';

import { CardBox, Text } from 'components';

export const CardGridContainer = () => {
  // TODO : 성공한 챌린지 GET API 연결
  const themeContext = useContext(ThemeContext);

  return (
    <div>
      <Text fontWeight="bold" size={24} color={themeContext.onBackground}>
        성공한 챌린지
      </Text>
      <Line />
      <Grid>
        {mySuccessChallenges.map((challenge) => (
          <CardBox key={challenge.challengeId} {...challenge} />
        ))}
      </Grid>
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
