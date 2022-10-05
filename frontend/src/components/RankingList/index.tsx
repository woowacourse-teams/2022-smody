import { RankingListSkeletonProps } from './type';
import useRankingList from './useRankingList';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text, RankingItem } from 'components';

export const RankingList = () => {
  const themeContext = useThemeContext();
  const { allRankingData, needSkeleton } = useRankingList();

  if (needSkeleton || typeof allRankingData?.data === 'undefined') {
    return (
      <Wrapper flexDirection="column" gap="0.5rem">
        <Text size={20} color={themeContext.onBackground} fontWeight="bold">
          전체 랭킹
        </Text>
        <RankingListSkeleton
          title="전체 랭킹을 불러오는 중입니다..."
          description="잠시만 기다려주세요!"
        />
      </Wrapper>
    );
  }

  if (allRankingData.data.length === 0) {
    return (
      <Wrapper flexDirection="column" gap="0.5rem">
        <Text size={20} color={themeContext.onBackground} fontWeight="bold">
          전체 랭킹
        </Text>
        <RankingListSkeleton
          title="아직 참여자가 없는 랭킹입니다 :)"
          description="인증하고 랭킹 1등 자리를 차지해보아요!! 🏆"
        />
      </Wrapper>
    );
  }

  return (
    <Wrapper flexDirection="column" gap="0.5rem">
      <Text size={20} color={themeContext.onBackground} fontWeight="bold">
        전체 랭킹
      </Text>
      <FlexBox as="ol" flexDirection="column" gap="0.5rem">
        {allRankingData.data.map((rankingData) => (
          <li key={rankingData.memberId}>
            <RankingItem {...rankingData} />
          </li>
        ))}
      </FlexBox>
    </Wrapper>
  );
};

const RankingListSkeleton = ({ title, description }: RankingListSkeletonProps) => {
  const themeContext = useThemeContext();

  return (
    <NonDataWrapper
      flexDirection="column"
      justifyContent="center"
      alignItems="center"
      gap="1rem"
    >
      <Text size={20} color={themeContext.onSurface} fontWeight="bold">
        {title}
      </Text>
      <Text size={16} color={themeContext.onSurface}>
        {description}
      </Text>
    </NonDataWrapper>
  );
};

const Wrapper = styled(FlexBox)`
  flex-grow: 1;
`;

const NonDataWrapper = styled(FlexBox)`
  ${({ theme }) => css`
    flex-grow: 1;
    height: 100%;
    border-radius: 1rem;
    background-color: ${theme.surface};
    padding: 0.5rem 1rem;
  `}
`;
