import { RankingList } from './components/RankingList';
import { RankingPeriodsList } from './components/RankingPeriodsList';
import { UserRanking } from './components/UserRanking';
import styled from 'styled-components';

import { FlexBox } from 'components';

const RankingPage = () => {
  return (
    <Wrapper flexDirection="column" gap="1rem">
      <RankingPeriodsList />
      <UserRanking />
      <RankingList />
    </Wrapper>
  );
};

export default RankingPage;

const Wrapper = styled(FlexBox)`
  flex: 1 1 auto;
`;
