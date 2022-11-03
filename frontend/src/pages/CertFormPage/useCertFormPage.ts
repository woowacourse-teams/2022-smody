import { usePostCycleProgress } from 'apis';
import { usePostMentionNotifications } from 'apis/feedApi';
import { useState, useRef, useMemo, FormEventHandler, ChangeEventHandler } from 'react';
import { useLocation } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isDarkState } from 'recoil/darkMode/atoms';

import useImageInput from 'hooks/useImageInput';
import useThemeContext from 'hooks/useThemeContext';

import { CertFormPageLocationState } from 'pages/CertFormPage/type';

const FORM_DATA_IMAGE_NAME = 'progressImage';

const useCertFormPage = () => {
  const editableElementRef = useRef<HTMLDivElement>(null);
  const [mentionedMemberIds, setMentionedMemberIds] = useState<Array<number>>([]);

  const isDark = useRecoilValue(isDarkState);
  const themeContext = useThemeContext();
  const [description, setDescription] = useState('');
  const [isSuccessModalOpen, setIsSuccessModalOpen] = useState(false);

  const {
    mutate: postMentionNotifications,
    isLoading: isLoadingPostMentionNotifications,
  } = usePostMentionNotifications();

  const {
    previewImageUrl,
    handleImageInputButtonClick,
    renderImageInput,
    hasImageFormData,
    isImageLoading,
    formData,
    textAreaRef,
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
    onSuccess: ({ data }) => {
      setIsSuccessModalOpen(true);

      if (mentionedMemberIds.length > 0) {
        postMentionNotifications({
          memberIds: Array.from(new Set(mentionedMemberIds)),
          pathId: Number(data.cycleDetailId),
        });
      }
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
    setDescription,
    previewImageUrl,
    handleImageInputButtonClick,
    renderImageInput,
    handleSubmitCert,
    handleCloseModal,
    editableElementRef,
    mentionedMemberIds,
    setMentionedMemberIds,
  };
};

export default useCertFormPage;
