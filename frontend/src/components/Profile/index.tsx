import useProfile from './useProfile';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text, Button } from 'components';

export const Profile = () => {
  const themeContext = useThemeContext();
  const { myInfo, myCyclesStat, handleClickEdit, handleClickLogout } = useProfile();

  if (typeof myInfo === 'undefined' || typeof myCyclesStat === 'undefined') {
    return null;
  }

  const { picture, nickname, introduction } = myInfo.data;
  const { totalCount, successCount } = myCyclesStat.data;

  return (
    <Wrapper flexDirection="column" justifyContent="center" gap="2.12rem">
      <FlexBox justifyContent="center" gap="2rem">
        <ProfileImg
          aria-label="프로필 이미지"
          src={picture}
          alt={`${nickname} 프로필 사진`}
        />
        <FlexBox flexDirection="column" gap="0.4rem">
          <Text
            aria-label="닉네임"
            size={20}
            color={themeContext.onBackground}
            fontWeight="bold"
          >
            {nickname}
          </Text>
          <Text aria-label="자기소개" size={16} color={themeContext.onBackground}>
            {introduction}
          </Text>
          <FlexBox gap="2.625rem">
            <FlexBox gap="0.4rem">
              <Text size={16} color={themeContext.onBackground}>
                성공
              </Text>
              <Text
                aria-label="성공한 챌린지 횟수"
                size={16}
                fontWeight="bold"
                color={themeContext.onBackground}
              >
                {successCount}
              </Text>
            </FlexBox>
            <FlexBox gap="0.4rem">
              <Text size={16} color={themeContext.onBackground}>
                전체
              </Text>
              <Text
                aria-label="시도한 챌린지 횟수"
                size={16}
                fontWeight="bold"
                color={themeContext.onBackground}
              >
                {totalCount}
              </Text>
            </FlexBox>
          </FlexBox>
        </FlexBox>
      </FlexBox>
      <FlexBox gap="0.56rem" justifyContent="center">
        <EditButton onClick={handleClickEdit}>프로필 편집</EditButton>
        <LogoutButton onClick={handleClickLogout}>로그아웃</LogoutButton>
      </FlexBox>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)`
  width: 100%;
  margin: 1rem 2.375rem 0;
`;

const ProfileImg = styled.img`
  width: 5.18rem;
  height: 5.18rem;
  border-radius: 50%;
`;

const EditButton = styled(Button).attrs({
  size: 'small',
})`
  flex-grow: 4;
`;

const LogoutButton = styled(Button).attrs({
  size: 'small',
  isActive: false,
})`
  flex-grow: 1;
`;
