import useUserRanking from './useUserRanking';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { Text, RankingItem, FlexBox } from 'components';

export const UserRanking = () => {
  const themeContext = useThemeContext();
  const { myRankingData, needSkeleton, notFoundInRanking } = useUserRanking();

  if (notFoundInRanking) {
    return (
      <FlexBox flexDirection="column" gap="0.5rem">
        <Text size={24} color={themeContext.onBackground} fontWeight="bold">
          나의 순위
        </Text>
        <NotFoundUserRankingItem />
      </FlexBox>
    );
  }

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

const NotFoundUserRankingItem = () => {
  const themeContext = useThemeContext();

  return (
    <Wrapper
      flexDirection="column"
      justifyContent="center"
      alignItems="center"
      gap="1rem"
    >
      <Text size={16} color={themeContext.onSurface} fontWeight="bold">
        참가하지 않은 랭킹입니다 :)
      </Text>
      <Text size={12} color={themeContext.onSurface}>
        인증하고 랭킹에 도전해보아요!! 🏆
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
