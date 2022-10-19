import { usePostChallenge } from 'apis';
import { ChangeEvent, FormEvent, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { validateChallengeDescription, validateChallengeName } from 'utils/validator';

import useInput from 'hooks/useInput';
import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

const NON_SELECTED = -1;

const useChallengeCreatePage = () => {
  const navigate = useNavigate();
  const renderSnackBar = useSnackBar();
  const challengeName = useInput('', validateChallengeName);
  const challengeDescription = useInput('', validateChallengeDescription);
  const [colorSelectedIndex, setColorSelectedIndex] = useState(NON_SELECTED);
  const [emojiSelectedIndex, setEmojiSelectedIndex] = useState(NON_SELECTED);

  const [isEmojiBottomSheetOpen, setIsEmojiBottomSheetOpen] = useState(false);

  const {
    mutate: mutatePostChallenge,
    isLoading: isLoadingPostChallenge,
    isSuccess: isSuccessPostChallenge,
  } = usePostChallenge({
    onSuccess: () => {
      renderSnackBar({
        message: `${challengeName.value} 챌린지를 생성하였습니다`,
        status: 'SUCCESS',
      });

      navigate(CLIENT_PATH.CHALLENGE_SEARCH);
    },
  });

  const isInvalidEmojiSelect =
    colorSelectedIndex === NON_SELECTED || emojiSelectedIndex === NON_SELECTED;

  const isAllValidated =
    challengeName.isValidated === true &&
    challengeDescription.isValidated === true &&
    !isInvalidEmojiSelect;

  const handleSelectColor = (event: ChangeEvent<HTMLInputElement>) => {
    const currentIndex = Number(event.target.value);

    if (colorSelectedIndex !== currentIndex) {
      setColorSelectedIndex(currentIndex);
    }
  };

  const handleSelectEmoji = (event: ChangeEvent<HTMLInputElement>) => {
    const currentIndex = Number(event.target.value);

    if (emojiSelectedIndex !== currentIndex) {
      setEmojiSelectedIndex(currentIndex);
    }
  };

  const handleClickCreateChallenge = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    if (
      typeof challengeName.value === 'undefined' ||
      typeof challengeDescription.value === 'undefined'
    ) {
      return null;
    }

    mutatePostChallenge({
      challengeName: challengeName.value,
      description: challengeDescription.value,
      emojiIndex: emojiSelectedIndex,
      colorIndex: colorSelectedIndex,
    });
  };

  const handleClickEmojiButton = () => {
    setIsEmojiBottomSheetOpen(true);
  };

  const handleCloseBottomSheet = () => {
    setIsEmojiBottomSheetOpen(false);
  };

  return {
    challengeName,
    challengeDescription,
    colorSelectedIndex,
    emojiSelectedIndex,
    isEmojiBottomSheetOpen,
    isLoadingPostChallenge,
    isSuccessPostChallenge,
    isInvalidEmojiSelect,
    isAllValidated,
    handleSelectColor,
    handleSelectEmoji,
    handleClickCreateChallenge,
    handleClickEmojiButton,
    handleCloseBottomSheet,
  };
};

export default useChallengeCreatePage;
