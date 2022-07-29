import React, { ChangeEventHandler } from 'react';
import styled, { ThemeContext } from 'styled-components';

import { useImageInput } from 'hooks/useImageInput';

import {
  FlexBox,
  Text,
  Button,
  Input,
  LoadingSpinner,
  UserWithdrawalModal,
} from 'components';

export const Test = () => {
  const {
    image,
    setImage,
    handleImageInputChange,
    sendImageToServer,
    imageInputRef,
    handleImageInputButtonClick,
  } = useImageInput();

  return (
    <>
      <ProfileImg src={image.previewUrl} />
      <input
        name="image"
        type="file"
        accept="image/*"
        hidden
        onChange={handleImageInputChange}
        ref={imageInputRef}
      />
      <Button onClick={handleImageInputButtonClick} size="medium" isActive={false}>
        이미지 선택
      </Button>
      <Button onClick={sendImageToServer} size="large" isActive={false}>
        서버 전송
      </Button>
    </>
  );
};

const ProfileImg = styled.img`
  width: 5.18rem;
  height: 5.18rem;
  border-radius: 50%;
  margin-bottom: 1rem;
`;
