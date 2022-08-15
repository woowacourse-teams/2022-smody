import useChallengeDetailPage from './useChallengeDetailPage';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import {
  FlexBox,
  FixedButton,
  ChallengerList,
  ChallengeDetail,
  Text,
  CustomCycleTimeBottomSheet,
} from 'components';

const ChallengeDetailPage = () => {
  const themeContext = useThemeContext();
  const {
    challengeData,
    joinChallenge,
    isCustomCycleTimeOpen,
    handleOpenBottomSheet,
    handleCloseBottomSheet,
  } = useChallengeDetailPage();

  if (typeof challengeData?.data === 'undefined') {
    return null;
  }

  const { challengeId, challengeName, isInProgress } = challengeData.data;

  return (
    <Wrapper flexDirection="column" gap="2rem">
      <ChallengeDetail {...challengeData.data} />
      <FlexBox flexDirection="column" gap="30px" style={{ height: '50vh' }}>
        <Text size={24} color={themeContext.onBackground} fontWeight="bold">
          챌린지 도전자
        </Text>
        <ChallengerList challengeId={challengeId} />
      </FlexBox>
      <FixedButton size="large" onClick={handleOpenBottomSheet} disabled={isInProgress}>
        {isInProgress ? '도전중' : '도전하기'}
      </FixedButton>
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

export default ChallengeDetailPage;

const Wrapper = styled(FlexBox)`
  margin: 0 1.25rem;
  padding-bottom: 90px;
`;
