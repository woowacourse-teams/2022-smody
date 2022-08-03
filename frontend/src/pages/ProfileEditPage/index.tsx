import { useGetMyInfo, usePatchMyInfo, usePostProfileImage } from 'apis';
import { FormEventHandler, MouseEventHandler, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { validateNickname, validateIntroduction } from 'utils/validator';

import useImageInput from 'hooks/useImageInput';
import useInput from 'hooks/useInput';
import useSnackBar from 'hooks/useSnackBar';
import useThemeContext from 'hooks/useThemeContext';

import {
  FlexBox,
  Button,
  Input,
  LoadingSpinner,
  UserWithdrawalModal,
  Title,
} from 'components';

import { CLIENT_PATH } from 'constants/path';

const FORM_DATA_IMAGE_NAME = 'profileImage';

const ProfileEditPage = () => {
  const { mutate: postProfileImage } = usePostProfileImage();
  const {
    previewImageUrl,
    handleImageInputButtonClick,
    renderImageInput,
    hasImageFormData,
    isImageLoading,
    formData,
  } = useImageInput(FORM_DATA_IMAGE_NAME);
  const navigate = useNavigate();
  const themeContext = useThemeContext();
  const renderSnackBar = useSnackBar();
  const [isOpenUserWithdrawalModal, setIsOpenUserWithdrawalModal] = useState(false);

  const { isFetching: isFetchingGetMyInfo, data: dataMyInfo } = useGetMyInfo({
    refetchOnWindowFocus: false,
  });

  const { isLoading: isLoadingPatchMyInfo, mutate: editMyInfo } = usePatchMyInfo({
    onSuccess: () => {
      renderSnackBar({
        message: '프로필 수정이 완료됐습니다.',
        status: 'SUCCESS',
      });

      navigate(CLIENT_PATH.PROFILE);
    },
  });

  const email = useInput(dataMyInfo && dataMyInfo.data.email);
  const nickname = useInput(dataMyInfo && dataMyInfo.data.nickname, validateNickname);
  const introduction = useInput(
    dataMyInfo && dataMyInfo.data.introduction,
    validateIntroduction,
  );

  const isAllValidated =
    nickname.isValidated && introduction.isValidated && !isImageLoading;

  if (isFetchingGetMyInfo || isLoadingPatchMyInfo || typeof dataMyInfo === 'undefined') {
    return <LoadingSpinner />;
  }

  const handleClickProfileEdit: FormEventHandler<HTMLFormElement> = async (event) => {
    event.preventDefault();

    if (hasImageFormData) {
      postProfileImage({ formData });
    }

    if (
      typeof nickname.value === 'undefined' ||
      typeof introduction.value === 'undefined'
    ) {
      return;
    }

    editMyInfo({
      nickname: nickname.value,
      introduction: introduction.value,
      picture: dataMyInfo.data.picture,
    });
  };

  const handleClickUserWithdrawal: MouseEventHandler<HTMLButtonElement> = () => {
    setIsOpenUserWithdrawalModal(true);
  };

  const handleCloseUserWithdrawalModal = () => {
    setIsOpenUserWithdrawalModal(false);
  };

  const { email: existingEmail, picture } = dataMyInfo.data;

  const profileImgAlt = `${nickname.value}님의 프로필 사진`;

  return (
    <Wrapper flexDirection="column" justifyContent="center" alignItems="center">
      <Title text="프로필 수정" linkTo={CLIENT_PATH.PROFILE} />
      <ProfileImg src={previewImageUrl || picture} alt={profileImgAlt} />
      {renderImageInput()}
      <Button onClick={handleImageInputButtonClick} size="medium" isActive={false}>
        이미지 선택
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
        onClick={handleClickUserWithdrawal}
        size="small"
      >
        회원 탈퇴
      </Button>
      {isOpenUserWithdrawalModal && (
        <UserWithdrawalModal
          email={existingEmail}
          handleCloseModal={handleCloseUserWithdrawalModal}
        />
      )}
    </Wrapper>
  );
};

export default ProfileEditPage;

const Wrapper = styled(FlexBox)`
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
