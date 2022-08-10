import { usePostChallenge } from 'apis';
import { ChangeEvent, FormEvent, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { validateChallengeDescription, validateChallengeName } from 'utils/validator';

import useInput from 'hooks/useInput';
import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

const useChallengeCreatePage = () => {
  const navigate = useNavigate();
  const renderSnackBar = useSnackBar();
  const challengeName = useInput('', validateChallengeName);
  const challengeDescription = useInput('', validateChallengeDescription);
  const [colorSelectedIndex, setColorSelectedIndex] = useState(-1);
  const [emojiSelectedIndex, setEmojiSelectedIndex] = useState(-1);

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

      navigate(CLIENT_PATH.SEARCH);
    },
  });

  const isAllValidated =
    challengeName.isValidated === true &&
    challengeDescription.isValidated === true &&
    colorSelectedIndex !== -1 &&
    emojiSelectedIndex !== -1;

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
      emoji: emojiSelectedIndex,
      color: colorSelectedIndex,
    });
  };

  return {
    challengeName,
    challengeDescription,
    colorSelectedIndex,
    emojiSelectedIndex,
    isLoadingPostChallenge,
    isSuccessPostChallenge,
    isAllValidated,
    handleSelectColor,
    handleSelectEmoji,
    handleClickCreateChallenge,
  };
};

export default useChallengeCreatePage;