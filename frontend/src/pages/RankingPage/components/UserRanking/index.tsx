import { RankingItem } from '../RankingItem';
import { RankingItemSkeletonProps } from './type';
import useUserRanking from './useUserRanking';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { Text, FlexBox } from 'components';

export const UserRanking = () => {
  const themeContext = useThemeContext();
  const { myRankingData, needSkeleton, notFoundInRanking, isLogin } = useUserRanking();

  if (!isLogin) {
    return (
      <FlexBox flexDirection="column" gap="0.5rem">
        <Text size={20} color={themeContext.onBackground} fontWeight="bold">
          나의 순위
        </Text>
        <RankingItemSkeleton
          title="로그인이 필요합니다 :)"
          description="로그인 후 순위를 확인해 주세요"
        />
      </FlexBox>
    );
  }

  if (notFoundInRanking) {
    return (
      <FlexBox flexDirection="column" gap="0.5rem">
        <Text size={20} color={themeContext.onBackground} fontWeight="bold">
          나의 순위
        </Text>
        <RankingItemSkeleton
          title="참가하지 않은 랭킹입니다 :)"
          description="인증하고 랭킹에 도전해보아요!! 🏆"
        />
      </FlexBox>
    );
  }

  if (needSkeleton || typeof myRankingData?.data === 'undefined') {
    return (
      <FlexBox flexDirection="column" gap="0.5rem">
        <Text size={20} color={themeContext.onBackground} fontWeight="bold">
          나의 순위
        </Text>
        <RankingItemSkeleton
          title="순위를 불러오는 중입니다..."
          description="잠시만 기다려주세요!"
        />
      </FlexBox>
    );
  }

  return (
    <FlexBox flexDirection="column" gap="0.5rem">
      <Text size={20} color={themeContext.onBackground} fontWeight="bold">
        나의 순위
      </Text>
      <RankingItem {...myRankingData.data} />
    </FlexBox>
  );
};

const RankingItemSkeleton = ({ title, description }: RankingItemSkeletonProps) => {
  const themeContext = useThemeContext();

  return (
    <Wrapper flexDirection="column" justifyContent="center" alignItems="center">
      <Text size={16} color={themeContext.onSurface} fontWeight="bold">
        {title}
      </Text>
      <Text size={12} color={themeContext.onSurface}>
        {description}
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
