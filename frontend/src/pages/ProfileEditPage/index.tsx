import { useGetMyInfo, usePatchMyInfo } from 'apis';
import { useContext, FormEventHandler } from 'react';
import { MdArrowBackIosNew } from 'react-icons/md';
import { useNavigate } from 'react-router-dom';
import styled, { ThemeContext } from 'styled-components';
import { validateNickname, validateIntroduction } from 'utils/validator';

import useInput from 'hooks/useInput';
import useSnackBar from 'hooks/useSnackBar';

import { FlexBox, Text, Button, Input, LoadingSpinner } from 'components';

import { CLIENT_PATH } from 'constants/path';

export const ProfileEditPage = () => {
  const navigate = useNavigate();
  const themeContext = useContext(ThemeContext);
  const renderSnackBar = useSnackBar();

  const {
    isFetching: isFetchingGetMyInfo,
    data: dataMyInfo,
    refetch,
  } = useGetMyInfo({
    refetchOnWindowFocus: false,
    onError: () => {
      renderSnackBar({
        message: '나의 정보 조회 시 에러가 발생했습니다.',
        status: 'ERROR',
      });

      // TODO: Error 페이지 렌더링 필요
    },
  });

  const { isLoading: isLoadingPatchMyInfo, mutate } = usePatchMyInfo({
    onSuccess: () => {
      refetch();
    },
    onError: () => {
      renderSnackBar({
        message: '나의 정보 수정 시 에러가 발생했습니다.',
        status: 'ERROR',
      });
    },
  });

  const email = useInput(dataMyInfo && dataMyInfo.data.email);
  const nickname = useInput(dataMyInfo && dataMyInfo.data.nickname, validateNickname);
  const introduction = useInput(
    dataMyInfo && dataMyInfo.data.introduction,
    validateIntroduction,
  );

  const isAllValidated = nickname.isValidated && introduction.isValidated;

  if (isFetchingGetMyInfo || isLoadingPatchMyInfo || typeof dataMyInfo === 'undefined') {
    return <LoadingSpinner />;
  }

  const backToPreviousPage = () => {
    navigate(CLIENT_PATH.PROFILE);
  };

  const handleClickProfileEdit: FormEventHandler<HTMLFormElement> = (event) => {
    event.preventDefault();

    if (
      typeof nickname.value === 'undefined' ||
      typeof introduction.value === 'undefined' ||
      typeof email.value === 'undefined'
    ) {
      return;
    }

    mutate({
      nickname: nickname.value,
      introduction: introduction.value,
      email: email.value,
    });
  };

  const { picture } = dataMyInfo.data;
  const profileImgAlt = `${nickname.value}님의 프로필 사진`;

  return (
    <Wrapper>
      <TitleWrapper onClick={backToPreviousPage}>
        <MdArrowBackIosNew size={20} />
        <Text fontWeight="bold" size={20} color={themeContext.onBackground}>
          프로필 편집
        </Text>
        <div />
      </TitleWrapper>
      <ProfileImg src={picture} alt={profileImgAlt} />
      <Button size="medium" isActive={false}>
        이미지 업로드
      </Button>
      <ProfileEditForm onSubmit={handleClickProfileEdit}>
        <Input disabled={true} type="email" label="이메일" placeholder="" {...email} />
        <Input
          type="text"
          label="닉네임"
          placeholder="닉네임을 입력해주세요"
          {...nickname}
        />
        <Input
          type="text"
          label="자기소개"
          placeholder="간단한 자기 소개를 입력해주세요."
          {...introduction}
        />
        <Button size="large" disabled={!isAllValidated}>
          프로필 편집 완료
        </Button>
      </ProfileEditForm>
      <Button
        style={{ backgroundColor: themeContext.error, color: themeContext.onError }}
        size="small"
      >
        회원 탈퇴
      </Button>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center',
})`
  padding: 3rem;
  /* PC (해상도 1024px)*/
  @media all and (min-width: 1024px) {
    padding: 1rem 10rem;
  }

  /* 테블릿 가로, 테블릿 세로 (해상도 768px ~ 1023px)*/
  @media all and (min-width: 768px) and (max-width: 1023px) {
    padding: 1rem 7rem;
  }

  /* 모바일 가로, 모바일 세로 (해상도 480px ~ 767px)*/
  @media all and (max-width: 767px) {
    padding: 1rem 1.25rem;
  }
`;

const TitleWrapper = styled(FlexBox).attrs({
  flexDirection: 'row',
  justifyContent: 'space-between',
})`
  width: 100%;
  margin-bottom: 2rem;
`;

const ProfileImg = styled.img`
  width: 5.18rem;
  height: 5.18rem;
  border-radius: 50%;
  margin-bottom: 1rem;
`;

const ProfileEditForm = styled.form`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  gap: 1.4rem;
  width: 100%;
  margin: 1rem 0;
`;
