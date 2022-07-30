import styled from 'styled-components';

import { useImageInput } from 'hooks/useImageInput';

import { Button } from 'components';

export const Test = () => {
  const { image, sendImageToServer, handleImageInputButtonClick, renderImageInput } =
    useImageInput();

  return (
    <>
      <ProfileImg
        src={image.previewUrl || 'https://www.w3schools.com/howto/img_avatar.png'}
      />
      {renderImageInput()}
      <Button onClick={handleImageInputButtonClick} size="medium" isActive={false}>
        이미지 선택
      </Button>
      <Button
        onClick={() => sendImageToServer('test-image')}
        size="large"
        isActive={false}
      >
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
