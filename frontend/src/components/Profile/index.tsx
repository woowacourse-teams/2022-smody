import { useGetMyCyclesStat, useGetMyInfo } from 'apis';
import { authApiClient } from 'apis/apiClient';
import { useContext, MouseEventHandler } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import styled, { ThemeContext } from 'styled-components';

import { FlexBox, Text, Button, LoadingSpinner } from 'components';

import { CLIENT_PATH } from 'constants/path';

export const Profile = () => {
  const themeContext = useContext(ThemeContext);

  const setIsLogin = useSetRecoilState(isLoginState);
  const navigate = useNavigate();
  const { data: dataMyInfo } = useGetMyInfo();
  const { data: dataMyCyclesStat } = useGetMyCyclesStat();

  if (typeof dataMyInfo === 'undefined' || typeof dataMyCyclesStat === 'undefined') {
    return <LoadingSpinner />;
  }

  const {
    data: { nickname, introduction, picture },
  } = dataMyInfo;
  const profileImgAlt = { nickname } + ' 프로필 사진';

  const {
    data: { totalCount, successCount },
  } = dataMyCyclesStat;

  const handleClickEdit: MouseEventHandler<HTMLButtonElement> = () => {
    navigate(CLIENT_PATH.PROFILE_EDIT);
  };

  const handleClickLogout: MouseEventHandler<HTMLButtonElement> = () => {
    authApiClient.deleteAuth();
    setIsLogin(false);
    navigate(CLIENT_PATH.HOME);
  };

  return (
    <Wrapper>
      <MyProfileWrapper>
        <ProfileImg src={picture} alt={profileImgAlt} />
        <ProfileDataWrapper>
          <Text size={20} color={themeContext.onBackground} fontWeight="bold">
            {nickname}
          </Text>
          {/* TODO: 프로필 편집 텍스트 width 제한. 이를 통해 자기 소개가 길어졌을 때 하단 프로필 편집, 로그아웃 버튼의 UI가 변경되는 문제 해결할 수 있음! */}
          <Text size={16} color={themeContext.onBackground}>
            {introduction}
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
        <EditButton onClick={handleClickEdit}>프로필 편집</EditButton>
        <LogoutButton onClick={handleClickLogout}>로그아웃</LogoutButton>
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
