import { useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styled, { ThemeContext } from 'styled-components';

import usePostJoinChallenge from 'hooks/api/usePostJoinChallenge';

import { Text, FlexBox, Button, ThumbnailWrapper } from 'components';
import { ChallengeItemProps } from 'components/ChallengeItem/type';

import { CLIENT_PATH } from 'constants/path';

export const ChallengeItem = ({
  challengeId,
  challengeName,
  challengerCount,
  isInProgress,
  challengeListRefetch,
}: ChallengeItemProps) => {
  const themeContext = useContext(ThemeContext);
  const { joinChallenge } = usePostJoinChallenge({
    challengeId: Number(challengeId),
    isNavigator: false,
    handleSuccessFunction: challengeListRefetch,
  });

  const navigate = useNavigate();
  const handleClickProgressButton = () => {
    isInProgress
      ? navigate(`${CLIENT_PATH.CHALLENGE_DETAIL}/${challengeId}`)
      : joinChallenge();
  };

  return (
    <Wrapper>
      <div>
        <Link to={`${CLIENT_PATH.CHALLENGE_DETAIL}/${challengeId}`}>
          <ChallengeInfoWrapper>
            <ThumbnailWrapper size="small" bgColor="#FED6D6">
              🌞
            </ThumbnailWrapper>
            <ChallengeNameWrapper>
              <Text color={themeContext.onSurface} size={16}>
                {challengeName}
              </Text>
              <Text color={themeContext.mainText} size={12}>
                {challengerCount}명 도전중
              </Text>
            </ChallengeNameWrapper>
          </ChallengeInfoWrapper>
        </Link>
      </div>
      <Button onClick={handleClickProgressButton} size="small" isActive={isInProgress}>
        {isInProgress ? '도전중' : '도전'}
      </Button>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  justifyContent: 'space-between',
  alignItems: 'center',
})`
  div {
    flex-grow: 1;
  }

  ${Button} {
    flex-grow: 0;
  }
`;

const ChallengeInfoWrapper = styled(FlexBox).attrs({
  gap: '17px',
  alignItems: 'center',
})``;

const ChallengeNameWrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  gap: '3px',
})``;
