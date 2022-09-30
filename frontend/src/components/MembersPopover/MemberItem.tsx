import { MemberItemProps } from './type';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';

export const MemberItem = ({ nickname, picture }: MemberItemProps) => {
  const themeContext = useThemeContext();

  return (
    <Wrapper gap="0.8rem">
      <ProfileImg
        aria-label="프로필 이미지"
        src={picture}
        alt={`${nickname} 프로필 사진`}
      />
      <Text size={16} color={themeContext.onBackground}>
        {nickname}
      </Text>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)`
  ${({ theme }) => css`
    padding: 0.2rem 1rem;
    cursor: pointer;
    &:hover {
      background-color: ${theme.primary};
    }
  `}
`;

const ProfileImg = styled.img`
  width: 1.5rem;
  height: 1.5rem;
  border-radius: 50%;
  object-fit: cover;
`;
