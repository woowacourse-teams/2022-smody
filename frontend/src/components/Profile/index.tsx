import useProfile from './useProfile';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text, Button } from 'components';

export const Profile = () => {
  const themeContext = useThemeContext();
  const {
    myInfo,
    myCyclesStat,
    handleClickEdit,
    handleClickLogout,
    isErrorMyInfo,
    isErrorMyCyclesStat,
  } = useProfile();

  if (isErrorMyInfo || isErrorMyCyclesStat) {
    return <h2>현재 오프라인 상태입니다.</h2>;
  }

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
                성공횟수
              </Text>
              <Text
                aria-label="성공한 챌린지 횟수"
                size={16}
                fontWeight="bold"
                color={themeContext.primary}
              >
                {successCount}
              </Text>
            </FlexBox>
            <FlexBox gap="0.4rem">
              <Text size={16} color={themeContext.onBackground}>
                도전횟수
              </Text>
              <Text
                aria-label="시도한 챌린지 횟수"
                size={16}
                fontWeight="bold"
                color={themeContext.primary}
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

  /* PC (해상도 1024px)*/
  /* 테블릿 가로, 테블릿 세로 (해상도 768px ~ 1023px)*/
  @media all and (min-width: 768px) {
    align-self: flex-start;
    position: sticky;
    left: 0;
    top: 50%;
    transform: translateY(-50%);
  }

  /* 모바일 가로, 모바일 세로 (해상도 480px ~ 767px)*/
  @media all and (max-width: 767px) {
    position: relative;
  }
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
