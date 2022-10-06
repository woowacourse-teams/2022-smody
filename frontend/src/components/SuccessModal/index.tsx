import useSuccessModal from './useSuccessModal';
import { useGetMyRanking } from 'apis';
import Close from 'assets/close.svg';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import {
  Button,
  FlexBox,
  ModalOverlay,
  Text,
  CheckCircles,
  ChangedNumberItem,
  UnderLineText,
} from 'components';
import { SuccessModalProps } from 'components/SuccessModal/type';

export const SuccessModal = ({
  handleCloseModal,
  cycleId,
  challengeName,
  successCount,
  challengeId,
  progressCount,
  emoji,
  myPreviousRank,
}: SuccessModalProps) => {
  const themeContext = useThemeContext();
  const {
    successMessage,
    isChallengeComplete,
    handleClickClose,
    handleClickCheck,
    handleClickShare,
    handleClickRetry,
  } = useSuccessModal({
    handleCloseModal,
    cycleId,
    challengeName,
    challengeId,
    progressCount,
    emoji,
  });

  const { data: myRankingData } = useGetMyRanking({ rankingPeriodId: 0 });
  const myCurrentRank = {
    point: myRankingData?.data.point,
    ranking: myRankingData?.data.ranking,
  };

  return (
    <ModalOverlay handleCloseModal={handleClickClose}>
      <Wrapper
        flexDirection="column"
        alignItems="center"
        justifyContent="center"
        gap="1rem"
      >
        <CloseWrapper onClick={handleClickClose}>
          <Close />
        </CloseWrapper>
        <FlexBox>
          <ChangedNumberItem
            from={myPreviousRank.ranking ?? 0}
            to={myCurrentRank.ranking!}
            isReverse={true}
            unit="위"
          />
          <Text color={themeContext.onSurface} size={70} fontWeight="normal">
            {emoji}
            <span id="confettiRewardId" />
            <span id="emojiRewardId" />
          </Text>
          <ChangedNumberItem
            from={myPreviousRank.point ?? 0}
            to={myCurrentRank.point!}
            unit="점"
          />
        </FlexBox>
        <UnderLineText
          fontColor={themeContext.onSurface}
          fontSize={24}
          underLineColor={themeContext.primary}
          fontWeight="bold"
        >
          {challengeName}
        </UnderLineText>
        <Text color={themeContext.primary} size={20} fontWeight="bold">
          {isChallengeComplete ? '🎉 챌린지 성공 🎉' : '오늘의 인증 완료'}
        </Text>
        <Text color={themeContext.onSurface} size={20} fontWeight="bold">
          {successMessage}
        </Text>
        {isChallengeComplete ? (
          <Text color={themeContext.disabled} size={16} fontWeight="normal">
            해당 챌린지를 총 {successCount}회 성공하셨어요.
          </Text>
        ) : (
          <CheckCircles progressCount={progressCount} />
        )}
        {isChallengeComplete ? (
          <FlexBox flexDirection="column" gap="1rem">
            <FlexBox gap="0.5rem">
              <Button onClick={handleClickCheck} size="medium" isActive={false}>
                기록 확인
              </Button>
              <Button onClick={handleClickShare} size="medium" isActive={false}>
                공유하기
              </Button>
            </FlexBox>
            <RetryButton onClick={handleClickRetry} size="medium" isActive={true}>
              재도전
            </RetryButton>
          </FlexBox>
        ) : (
          <Button autoFocus onClick={handleClickCheck} size="medium" isActive={false}>
            기록 확인
          </Button>
        )}
      </Wrapper>
    </ModalOverlay>
  );
};

const Wrapper = styled(FlexBox)`
  width: 100%;
  padding: 1rem 1.25rem 1.438rem;
`;

const CloseWrapper = styled.div`
  align-self: flex-end;
  cursor: pointer;
`;

const RetryButton = styled(Button)`
  width: 100%;
`;
