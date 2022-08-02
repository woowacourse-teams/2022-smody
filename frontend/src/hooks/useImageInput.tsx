import imageCompression from 'browser-image-compression';
import { ChangeEvent, useEffect, useRef, useState, useMemo } from 'react';

const compressionOptions = {
  maxSizeMB: 0.2,
  maxWidthOrHeight: 1920,
  useWebWorker: true,
};

const useImageInput = (imageName: string) => {
  const [formData, setFormData] = useState(new FormData());
  const [isImageLoading, setIsImageLoading] = useState(false);
  const [previewUrl, setPreviewUrl] = useState('');
  const imageInputRef = useRef<HTMLInputElement>(null);

  const reader = useMemo(() => new FileReader(), []);

  useEffect(() => {
    reader.addEventListener('loadend', handleReaderLoadend);

    // 컴포넌트가 언마운트되면 createObjectURL()을 통해 생성한 기존 URL을 폐기
    return () => {
      URL.revokeObjectURL(previewUrl);
      reader.removeEventListener('loadend', handleReaderLoadend);
    };
  }, []);

  const handleReaderLoadend = async () => {
    console.log('hihihi');
    const dataURL = reader.result;

    if (typeof dataURL !== 'string') {
      return;
    }

    const encodedData = encodeFormData(dataURL);
    setFormData(encodedData);
    setIsImageLoading(false);
    console.log('@@@@', dataURL);
  };

  const handleImageInputButtonClick = () => {
    imageInputRef?.current?.click();
  };

  const handleImageInputChange = async (event: ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();

    const file = event.currentTarget.files?.[0];

    if (!file) {
      return;
    }

    setIsImageLoading(true);

    URL.revokeObjectURL(previewUrl);
    const newPreviewUrl = URL.createObjectURL(file);

    setPreviewUrl(newPreviewUrl);

    const compressedFile = await imageCompression(file, compressionOptions);

    reader.readAsDataURL(compressedFile);
  };

  const encodeFormData = (dataURL: string): FormData => {
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

  return {
    previewImageUrl: previewUrl,
    handleImageInputButtonClick,
    renderImageInput,
    isImageLoading,
    formData,
  };
};

export default useImageInput;
