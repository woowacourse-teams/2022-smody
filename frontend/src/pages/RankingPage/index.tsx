import { FlexBox, RankingPeriodsList, UserRanking } from 'components';

const RankingPage = () => {
  return (
    <FlexBox flexDirection="column" gap="1rem">
      <UserRanking />
      <RankingPeriodsList />
    </FlexBox>
  );
};

export default RankingPage;
