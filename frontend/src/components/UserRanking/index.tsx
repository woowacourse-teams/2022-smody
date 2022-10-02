import { UserRankingProps } from './type';
import useUserRanking from './useUserRanking';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { Text, RankingItem, FlexBox } from 'components';

export const UserRanking = ({ rankingPeriodId }: UserRankingProps) => {
  const themeContext = useThemeContext();
  const { myRankingData, needSkeleton } = useUserRanking({ rankingPeriodId });

  if (needSkeleton || typeof myRankingData?.data === 'undefined') {
    return (
      <FlexBox flexDirection="column" gap="0.5rem">
        <Text size={24} color={themeContext.onBackground} fontWeight="bold">
          나의 순위
        </Text>
        <UserRankingItemSkeleton />
      </FlexBox>
    );
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

const UserRankingItemSkeleton = () => {
  const themeContext = useThemeContext();
  console.log('inskeleton');

  return (
    <Wrapper
      flexDirection="column"
      justifyContent="center"
      alignItems="center"
      gap="1rem"
    >
      <Text size={16} color={themeContext.onSurface} fontWeight="bold">
        순위를 불러오는 중입니다...
      </Text>
      <Text size={12} color={themeContext.onSurface}>
        잠시만 기다려주세요!
      </Text>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)`
  ${({ theme }) => css`
    border-radius: 1rem;
    background-color: ${theme.surface};
    padding: 0.5rem 1rem;
  `}
`;
