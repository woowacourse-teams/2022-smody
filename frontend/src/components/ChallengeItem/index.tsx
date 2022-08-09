import { queryKeys } from 'apis/constants';
import { useQueryClient } from 'react-query';
import { Link, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { getEmoji } from 'utils/emoji';

import useThemeContext from 'hooks/useThemeContext';

import { Text, FlexBox, Button, ThumbnailWrapper } from 'components';
import { ChallengeItemProps } from 'components/ChallengeItem/type';

import { CLIENT_PATH } from 'constants/path';

export const ChallengeItem = ({
  challengeId,
  challengeName,
  challengerCount,
  isInProgress,
}: ChallengeItemProps) => {
  const navigate = useNavigate();
  const navigate = useNavigate();
  const themeContext = useThemeContext();
  const queryClient = useQueryClient();

  const { joinChallenge } = usePostJoinChallenge({
    challengeId: Number(challengeId),
    successCallback: () => {
      queryClient.invalidateQueries(queryKeys.getAllChallenges);
    },
  });

  const navigate = useNavigate();
  const handleClickProgressButton = () => {
    isInProgress
      ? navigate(`${CLIENT_PATH.CHALLENGE_DETAIL}/${challengeId}`)
      : joinChallenge(challengeName);
  };

  return (
    <Wrapper justifyContent="space-between" alignItems="center">
      <div>
        <Link to={`${CLIENT_PATH.CHALLENGE_DETAIL}/${challengeId}`}>
          <FlexBox gap="17px" alignItems="center">
            <ThumbnailWrapper size="small" bgColor="#E6D1F2">
              {getEmoji(Number(challengeId))}
            </ThumbnailWrapper>
            <FlexBox flexDirection="column" gap="0.5rem">
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
      <Button onClick={handleClickProgressButton} size="small" isActive={!isInProgress}>
        {isInProgress ? '도전중' : '도전'}
      </Button>
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
