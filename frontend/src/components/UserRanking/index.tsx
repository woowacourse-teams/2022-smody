import { UserRankingProps } from './type';
import useUserRanking from './useUserRanking';

import useThemeContext from 'hooks/useThemeContext';

import { Text, RankingItem, FlexBox } from 'components';

export const UserRanking = ({ rankingPeriodId }: UserRankingProps) => {
  const themeContext = useThemeContext();
  const { myRankingData } = useUserRanking({ rankingPeriodId });

  if (typeof myRankingData?.data === 'undefined') {
    return null;
  }

  return (
    <FlexBox flexDirection="column" gap="0.5rem">
      <Text size={24} color={themeContext.onBackground} fontWeight="bold">
        나의 순위
      </Text>
      <RankingItem {...myRankingData.data} />
    </FlexBox>
  );
};
