import { MedalProps, RankingItemProps, WrapperProps } from './type';
import useRankingItem from './useRankingItem';
import { FaMedal } from 'react-icons/fa';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';

export const RankingItem = ({
  ranking,
  memberId,
  nickname,
  introduction,
  picture,
  point,
}: RankingItemProps) => {
  const { surfaceColor, onSurfaceColor } = useRankingItem({ memberId });

  return (
    <Wrapper surfaceColor={surfaceColor} alignItems="center" gap="1rem">
      <RankingText size={24} color={onSurfaceColor} fontWeight="bold">
        {ranking}
      </RankingText>
      <ProfileImg
        aria-label="프로필 이미지"
        src={picture}
        alt={`${nickname} 프로필 사진`}
        loading="lazy"
      />
      <UserInfoWrapper flexDirection="column">
        <Text aria-label="닉네임" size={16} color={onSurfaceColor} fontWeight="bold">
          {nickname}
        </Text>
        <Text aria-label="자기소개" size={12} color={onSurfaceColor}>
          {introduction}
        </Text>
      </UserInfoWrapper>
      <PointWrapper gap="1rem" alignItems="center">
        <Medal ranking={ranking} />
        <Text size={16} color={onSurfaceColor}>
          {point}점
        </Text>
      </PointWrapper>
    </Wrapper>
  );
};

const Medal = ({ ranking }: MedalProps) => {
  const themeContext = useThemeContext();

  if (ranking > 3) {
    return null;
  }

  if (ranking === 1) {
    return <FaMedal size={25} color={themeContext.first} />;
  }

  if (ranking === 2) {
    return <FaMedal size={25} color={themeContext.second} />;
  }

  return <FaMedal size={25} color={themeContext.third} />;
};

const Wrapper = styled(FlexBox)<WrapperProps>`
  ${({ surfaceColor }) => css`
    border-radius: 1rem;
    background-color: ${surfaceColor};
    padding: 0.5rem 1rem;
  `}
`;

const RankingText = styled(Text)`
  width: 1.8rem;
  text-align: center;
`;

const ProfileImg = styled.img`
  width: 2.6rem;
  height: 2.6rem;
  border-radius: 50%;
  object-fit: cover;
`;

const UserInfoWrapper = styled(FlexBox)`
  flex-grow: 1;
`;

const PointWrapper = styled(FlexBox)`
  justify-self: end;
`;
