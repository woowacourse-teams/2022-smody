import { authApiClient } from 'apis/apiClient';
import imageCompression from 'browser-image-compression';
import { ChangeEvent, useEffect, useRef, useState } from 'react';

export const useImageInput = () => {
  const imageInputRef = useRef<HTMLInputElement>(null);
  const handleImageInputButtonClick = () => {
    imageInputRef?.current?.click();
  };

  const [image, setImage] = useState({
    imageFile: {},
    previewUrl: '',
  });

  const handleImageInputChange = (event: ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      URL.revokeObjectURL(image.previewUrl);
      const previewUrl = URL.createObjectURL(file);

      setImage(() => ({
        imageFile: file,
        previewUrl,
      }));
    }
  };

  const sendImageToServer = async () => {
    console.log('압축 시작');
    if (!image.imageFile) {
      return;
    }

    const options = {
      maxSizeMB: 0.2,
      maxWidthOrHeight: 1920,
      useWebWorker: true,
    };
    try {
      const compressedFile = await imageCompression(image.imageFile as File, options);

      // FileReader 는 File 혹은 Blob 객체를 이용하여, 파일의 내용을 읽을 수 있게 해주는 Web API
      const reader = new FileReader();
      reader.readAsDataURL(compressedFile);
      reader.onloadend = async () => {
        // 변환 완료!
        const base64data = reader.result;

        // formData 만드는 함수
        console.log('base64data', base64data);
        makeFormData(base64data as string);
      };
    } catch (error) {
      console.log(error);
    }
  };

  const makeFormData = (dataURI: string) => {
    // dataURL 값이 data:image/jpeg:base64,~~~~~~~ 이므로 ','를 기점으로 잘라서 ~~~~~인 부분만 다시 인코딩
    const byteString = atob(dataURI.split(',')[1]);

    // Blob를 구성하기 위한 준비
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }
    const blob = new Blob([ia], {
      type: 'image/jpeg',
    });
    const file = new File([blob], 'image.jpg');
    // 위 과정을 통해 만든 image폼을 FormData에 넣어줍니다.
    // 서버에서는 이미지를 받을 때, FormData가 아니면 받지 않도록 세팅해야합니다.

    const formData = new FormData();
    formData.append('image', file);

    console.log('압축된 formData', formData);
    authApiClient.axios.post('/images', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  };

  useEffect(() => {
    // 컴포넌트가 언마운트되면 createObjectURL()을 통해 생성한 기존 URL을 폐기
    return () => {
      URL.revokeObjectURL(image.previewUrl);
    };
  }, []);

  return {
    image,
    setImage,
    handleImageInputChange,
    sendImageToServer,
    imageInputRef,
    handleImageInputButtonClick,
  };
};
