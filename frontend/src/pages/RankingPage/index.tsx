import styled from 'styled-components';

import { FlexBox, RankingPeriodsList, UserRanking, RankingList } from 'components';

const RankingPage = () => {
  return (
    <Wrapper flexDirection="column" gap="1rem">
      <UserRanking />
      <RankingPeriodsList />
      <RankingList />
    </Wrapper>
  );
};

export default RankingPage;

const Wrapper = styled(FlexBox)`
  flex: 1 1 auto;
`;
