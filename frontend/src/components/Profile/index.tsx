import { useGetMyCyclesStat, useGetMyInfo } from 'apis';
import { authApiClient } from 'apis/apiClient';
import { useContext, MouseEventHandler } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import styled, { ThemeContext } from 'styled-components';

import { useManageAccessToken } from 'hooks/useManageAccessToken';

import { FlexBox, Text, Button, LoadingSpinner } from 'components';

import { CLIENT_PATH } from 'constants/path';

export const Profile = () => {
  const themeContext = useContext(ThemeContext);
  const checkLogout = useManageAccessToken();

  const setIsLogin = useSetRecoilState(isLoginState);
  const navigate = useNavigate();
  const { isLoading: isLoadingMyInfo, data: dataMyInfo } = useGetMyInfo({
    onError: (error) => {
      checkLogout(error);
    },
  });
  const { isLoading: isLoadingMyCyclesStat, data: dataMyCyclesStat } =
    useGetMyCyclesStat();

  if (
    isLoadingMyInfo ||
    isLoadingMyCyclesStat ||
    typeof dataMyInfo === 'undefined' ||
    typeof dataMyCyclesStat === 'undefined'
  ) {
    return <LoadingSpinner />;
  }

  const {
    data: { nickname, picture },
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
    navigate(CLIENT_PATH.CERT);
  };

  return (
    <Wrapper>
      <MyProfileWrapper>
        <ProfileImg src={picture} alt={profileImgAlt} />
        <ProfileDataWrapper>
          <Text size={20} color={themeContext.onBackground} fontWeight="bold">
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
