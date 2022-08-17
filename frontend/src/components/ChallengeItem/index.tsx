import useChallengeItem from './useChallengeItem';
import { Link } from 'react-router-dom';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import {
  Text,
  FlexBox,
  Button,
  ThumbnailWrapper,
  CustomCycleTimeBottomSheet,
} from 'components';
import { ChallengeItemProps } from 'components/ChallengeItem/type';

import { CLIENT_PATH } from 'constants/path';
import { colorList, emojiList } from 'constants/style';

export const ChallengeItem = ({
  challengeId,
  challengeName,
  challengerCount,
  isInProgress,
  emojiIndex,
  colorIndex,
}: ChallengeItemProps) => {
  const themeContext = useThemeContext();
  const {
    joinChallenge,
    isCustomCycleTimeOpen,
    handleOpenBottomSheet,
    handleCloseBottomSheet,
  } = useChallengeItem({ challengeId });

  return (
    <Wrapper justifyContent="space-between" alignItems="center">
      <div>
        <Link to={`${CLIENT_PATH.CHALLENGE_DETAIL}/${challengeId}`}>
          <FlexBox gap="17px" alignItems="center">
            <ThumbnailWrapper size="small" bgColor={colorList[colorIndex]}>
              {emojiList[emojiIndex]}
            </ThumbnailWrapper>
            <FlexBox flexDirection="column">
              <Text aria-label="challenge-name" color={themeContext.onSurface} size={16}>
                {challengeName}
              </Text>
              <Text color={themeContext.mainText} size={12}>
                {challengerCount}명 도전중
              </Text>
            </FlexBox>
          </FlexBox>
        </Link>
      </div>
      <ProgressButton
        onClick={handleOpenBottomSheet}
        size="small"
        disabled={isInProgress}
      >
        {isInProgress ? '도전중' : '도전'}
      </ProgressButton>
      {isCustomCycleTimeOpen && (
        <CustomCycleTimeBottomSheet
          challengeName={challengeName}
          joinChallenge={joinChallenge}
          handleCloseBottomSheet={handleCloseBottomSheet}
        />
      )}
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)`
  div {
    flex-grow: 1;
  }
  ${Button} {
    flex-grow: 0;
  }
`;

const ProgressButton = styled(Button)`
  width: 68px;
`;
