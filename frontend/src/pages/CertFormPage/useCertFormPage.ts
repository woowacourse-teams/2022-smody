import { usePostCycleProgress } from 'apis';
import { useState, useMemo, FormEventHandler, ChangeEvent } from 'react';
import { useLocation } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isDarkState } from 'recoil/darkMode/atoms';

import useImageInput from 'hooks/useImageInput';
import useThemeContext from 'hooks/useThemeContext';

import { CertFormPageLocationState } from 'pages/CertFormPage/type';

const FORM_DATA_IMAGE_NAME = 'progressImage';

const useCertFormPage = () => {
  const isDark = useRecoilValue(isDarkState);
  const themeContext = useThemeContext();
  const [description, setDescription] = useState('');
  const [isSuccessModalOpen, setIsSuccessModalOpen] = useState(false);

  const {
    previewImageUrl,
    handleImageInputButtonClick,
    renderImageInput,
    hasImageFormData,
    isImageLoading,
    formData,
  } = useImageInput(FORM_DATA_IMAGE_NAME);

  const isButtonDisabled = useMemo(
    () => isImageLoading || !hasImageFormData,
    [isImageLoading, formData],
  );

  const {
    mutate: postCycleProgress,
    isLoading: isLoadingPost,
    isSuccess: isSuccessPost,
  } = usePostCycleProgress({
    onSuccess: () => {
      setIsSuccessModalOpen(true);
    },
  });

  const { state } = useLocation();

  const {
    cycleId,
    challengeId,
    challengeName,
    successCount,
    progressCount,
    emojiIndex,
    colorIndex,
  } = state as CertFormPageLocationState;

  const handleSubmitCert: FormEventHandler<HTMLFormElement> = (event) => {
    event.preventDefault();

    formData.append(
      'description',
      description.trim() || `${challengeName}챌린지 ${progressCount + 1}번째 완료`,
    );

    postCycleProgress({ cycleId, formData });
  };

  const handleCloseModal = () => {
    setIsSuccessModalOpen(false);
  };

  const handleChangeDescription = (event: ChangeEvent<HTMLTextAreaElement>) => {
    setDescription(event.target.value);
  };

  return {
    themeContext,
    isDark,
    cycleId,
    challengeId,
    challengeName,
    successCount,
    progressCount,
    emojiIndex,
    colorIndex,
    isButtonDisabled,
    isLoadingPost,
    isSuccessPost,
    isSuccessModalOpen,
    description,
    previewImageUrl,
    handleImageInputButtonClick,
    renderImageInput,
    handleSubmitCert,
    handleCloseModal,
    handleChangeDescription,
  };
};

export default useCertFormPage;
