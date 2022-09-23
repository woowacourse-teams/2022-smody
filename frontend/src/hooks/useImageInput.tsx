import imageCompression from 'browser-image-compression';
import { ChangeEvent, useEffect, useRef, useState, useMemo } from 'react';

interface CompressionOptions {
  maxSizeMB: number;
  maxWidthOrHeight: number;
}

// dataURL을 FormData로 인코딩하는 함수
const encodeFormData = (dataURL: string, imageName: string): FormData => {
  // dataURL 값이 data:image/jpeg:base64,~~이므로 ','를 기점으로 잘라서 ~~인 부분만 다시 인코딩
  const byteString = atob(dataURL.split(',')[1]);

  // Blob을 file로 구성하기 위한 준비
  const ab = new ArrayBuffer(byteString.length);
  const ia = new Uint8Array(ab);
  for (let i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i);
  }
  const blob = new Blob([ia]);

  // Blob을 File로 만든다
  const file = new File([blob], `${imageName}.jpg`, { type: 'image/jpeg' });

  // File을 FormData로 만든다
  const formData = new FormData();
  formData.append(imageName, file);

  return formData;
};

const useImageInput = (imageName: string) => {
  const [formData, setFormData] = useState(new FormData());
  const [isImageLoading, setIsImageLoading] = useState(false);
  const [previewImageUrl, setPreviewImageUrl] = useState('');
  const imageInputRef = useRef<HTMLInputElement>(null);
  const reader = useMemo(() => new FileReader(), []);

  const hasImageFormData = formData.get(imageName) !== null;

  useEffect(() => {
    // reader가 Blob을 dataURL로 읽는 작업이 끝나면 실행하는 이벤트 부착
    reader.addEventListener('loadend', handleReaderLoadend);

    return () => {
      // 컴포넌트가 언마운트되면 revokeObjectURL()을 통해 생성한 기존 URL을 폐기
      URL.revokeObjectURL(previewImageUrl);
      // 컴포넌트가 언마운트되면 reader에 부착된 이벤트를 제거
      reader.removeEventListener('loadend', handleReaderLoadend);
    };
  }, []);

  const handleReaderLoadend = async () => {
    const dataURL = reader.result;

    if (typeof dataURL !== 'string') {
      return;
    }

    // dataUrl을 File로 encode하여 FormData에 추가
    const formData = encodeFormData(dataURL, imageName);
    setFormData(formData);
    setIsImageLoading(false);
  };

  // 이미지 선택 시 이벤트 처리
  const handleImageInputChange = async (
    event: ChangeEvent<HTMLInputElement>,
    compressionOptions: CompressionOptions,
  ) => {
    event.preventDefault();

    const file = event.currentTarget.files?.[0];

    if (!file) {
      return;
    }

    setIsImageLoading(true);

    // 기존 preview 이미지 url 폐기
    URL.revokeObjectURL(previewImageUrl);

    // preview 이미지 url 생성
    const newPreviewImageUrl = URL.createObjectURL(file);

    setPreviewImageUrl(newPreviewImageUrl);

    // 이미지 압축하여 변환된 Blob
    const compressedBlob = await imageCompression(file, compressionOptions);

    // reader로 blob을 dataURL로 읽기 시작(끝나면 reader에 부착된 loadend 이벤트 발생)
    reader.readAsDataURL(compressedBlob);
  };

  //  이미지 인풋에 부착된 ref로 클릭 이벤트 전달
  const handleImageInputButtonClick = () => {
    imageInputRef?.current?.click();
  };

  // ref가 부착된 인풋을 render하는 함수
  const renderImageInput = (compressionOptions: CompressionOptions) => (
    <input
      name={imageName}
      type="file"
      accept="image/*"
      hidden
      onChange={(event) => handleImageInputChange(event, compressionOptions)}
      ref={imageInputRef}
    />
  );

  return {
    previewImageUrl,
    handleImageInputButtonClick,
    renderImageInput,
    hasImageFormData,
    isImageLoading,
    formData,
  };
};

export default useImageInput;
