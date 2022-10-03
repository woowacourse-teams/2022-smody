import { MedalProps, RankingItemProps } from './type';
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
  const themeContext = useThemeContext();

  return (
    <Wrapper alignItems="center" gap="1rem">
      <Text size={24} color={themeContext.onSurface} fontWeight="bold">
        {ranking}
      </Text>
      <ProfileImg
        aria-label="프로필 이미지"
        src={picture}
        alt={`${nickname} 프로필 사진`}
        loading="lazy"
      />
      <UserInfoWrapper flexDirection="column" gap="0.4rem">
        <Text
          aria-label="닉네임"
          size={16}
          color={themeContext.onSurface}
          fontWeight="bold"
        >
          {nickname}
        </Text>
        <Text aria-label="자기소개" size={12} color={themeContext.onSurface}>
          {introduction}
        </Text>
      </UserInfoWrapper>
      <PointWrapper gap="1rem" alignItems="center">
        <Medal ranking={ranking} />
        <FlexBox flexDirection="row">
          <Text size={20} color={themeContext.primary} fontWeight="bold">
            {point}
          </Text>
          <Text size={20} color={themeContext.onSurface} fontWeight="bold">
            p
          </Text>
        </FlexBox>
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

const Wrapper = styled(FlexBox)`
  ${({ theme }) => css`
    border-radius: 1rem;
    background-color: ${theme.surface};
    padding: 0.5rem 1rem;
  `}
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
