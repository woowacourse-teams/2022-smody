import imageCompression from 'browser-image-compression';
import { ChangeEvent, useEffect, useRef, useState } from 'react';

const compressionOptions = {
  maxSizeMB: 0.2,
  maxWidthOrHeight: 1920,
  useWebWorker: true,
};

const useImageInput = (imageName: string) => {
  const imageInputRef = useRef<HTMLInputElement>(null);
  const handleImageInputButtonClick = () => {
    imageInputRef?.current?.click();
  };

  const [image, setImage] = useState({
    imageFile: {},
    previewUrl: '',
  });

  useEffect(() => {
    // 컴포넌트가 언마운트되면 createObjectURL()을 통해 생성한 기존 URL을 폐기
    return () => {
      URL.revokeObjectURL(image.previewUrl);
    };
  }, []);

  const handleImageInputChange = (event: ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    const file = event.currentTarget.files?.[0];

    if (!file) {
      return;
    }

    URL.revokeObjectURL(image.previewUrl);
    const previewUrl = URL.createObjectURL(file);

    setImage(() => ({
      imageFile: file,
      previewUrl,
    }));
  };

  const renderImageInput = () => (
    <input
      name={imageName}
      type="file"
      accept="image/*"
      hidden
      onChange={handleImageInputChange}
      ref={imageInputRef}
    />
  );

  const isEmptyObj = (obj: object) => {
    if (obj.constructor === Object && Object.keys(obj).length === 0) {
      return true;
    }

    return false;
  };

  const sendImageToServer = async (postImage: (formData: FormData) => void) => {
    console.log(image.imageFile);
    if (isEmptyObj(image.imageFile)) {
      return;
    }

    const compressedFile = await imageCompression(
      image.imageFile as File,
      compressionOptions,
    );

    // FileReader 는 File 혹은 Blob 객체를 이용하여, 파일의 내용을 읽을 수 있게 해주는 Web API
    const reader = new FileReader();
    reader.readAsDataURL(compressedFile);
    reader.onloadend = async () => {
      // 변환 완료!
      const dataURL = reader.result;

      if (typeof dataURL !== 'string') {
        return;
      }

      const formData = makeFormData(dataURL);

      postImage(formData);
    };
  };

  const makeFormData = (dataURL: string): FormData => {
    // dataURL 값이 data:image/jpeg:base64,~~이므로 ','를 기점으로 잘라서 ~~인 부분만 다시 인코딩
    const byteString = atob(dataURL.split(',')[1]);

    // Blob를 구성하기 위한 준비
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }
    const blob = new Blob([ia], {
      type: 'image/jpeg',
    });
    const file = new File([blob], `${imageName}.jpg`);

    const formData = new FormData();
    formData.append(imageName, file);
    return formData;
  };

  return {
    previewImageUrl: image.previewUrl,
    sendImageToServer,
    handleImageInputButtonClick,
    renderImageInput,
  };
};

export default useImageInput;
