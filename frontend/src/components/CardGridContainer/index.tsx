import useCardGridContainer from './useCardGridContainer';
import { Challenge } from 'commonType';
import styled from 'styled-components';
import { getEmoji } from 'utils/emoji';

import useThemeContext from 'hooks/useThemeContext';

import { CardBox, Text, EmptyContent } from 'components';

import { CLIENT_PATH } from 'constants/path';

export const CardGridContainer = () => {
  const themeContext = useThemeContext();
  const { data, loadMore } = useCardGridContainer();

  if (data?.pages[0].data.length === 0) {
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
        {data?.pages.map((page) =>
          page?.data?.map((challenge: Challenge) => (
            <CardBox
              key={challenge.challengeId}
              {...challenge}
              bgColor="#E6D1F2"
              emoji={getEmoji(challenge.challengeId)}
            />
          )),
        )}
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
