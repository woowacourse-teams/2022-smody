import CuteCatWithSmody from 'assets/cute_cat_with_smody.png';
import { useContext } from 'react';
import styled, { ThemeContext } from 'styled-components';

import { FlexBox, Text, Button } from 'components';

const userData = {
  nickname: '마르코',
  picture: CuteCatWithSmody,
  email: 'marco@gmail.com',
};

const myCycleData = {
  totalCount: 35,
  successCount: 5,
};

export const Profile = () => {
  const themeContext = useContext(ThemeContext);
  const { nickname, picture } = userData;
  const { totalCount, successCount } = myCycleData;
  const profileImgAlt = { nickname } + ' 프로필 사진';

  return (
    <Wrapper>
      <MyProfileWrapper>
        <ProfileImg src={picture} alt={profileImgAlt} />
        <ProfileDataWrapper>
          <Text size={24} color={themeContext.onBackground} fontWeight="bold">
            {nickname}
          </Text>
          <Text size={16} color={themeContext.onBackground}>
            안녕하세요 {nickname}입니다
          </Text>
          <CycleWrapper>
            <CycleCountWrapper>
              <Text size={16} color={themeContext.onBackground}>
                성공
              </Text>
              <Text size={16} fontWeight="bold" color={themeContext.onBackground}>
                {successCount}
              </Text>
            </CycleCountWrapper>
            <CycleCountWrapper>
              <Text size={16} color={themeContext.onBackground}>
                전체
              </Text>
              <Text size={16} fontWeight="bold" color={themeContext.onBackground}>
                {totalCount}
              </Text>
            </CycleCountWrapper>
          </CycleWrapper>
        </ProfileDataWrapper>
      </MyProfileWrapper>
      <UserButtonWrapper>
        <EditButton>프로필 편집</EditButton>
        <LogoutButton>로그아웃</LogoutButton>
      </UserButtonWrapper>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  justifyContent: 'center',
  gap: '2.12rem',
})`
  width: 100%;
  margin: 1rem 2.375rem 0;
`;

const MyProfileWrapper = styled(FlexBox).attrs({
  justifyContent: 'center',
  gap: '2rem',
})``;

const ProfileImg = styled.img`
  width: 5.18rem;
  height: 5.18rem;
  border-radius: 50%;
`;

const ProfileDataWrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  gap: '0.4rem',
})``;

const CycleWrapper = styled(FlexBox).attrs({
  gap: '2.625rem',
})``;

const CycleCountWrapper = styled(FlexBox).attrs({
  gap: '0.4rem',
})``;

const UserButtonWrapper = styled(FlexBox).attrs({
  gap: '0.56rem',
  justifyContent: 'center',
})``;

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
