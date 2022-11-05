import useCertItem from './useCertItem';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text, Button, CheckCircles, Timer, ChallengeIcon } from 'components';
import { CertItemProps } from 'components/CertItem/type';

import { colorList, cursorPointer } from 'constants/style';

export const CertItem = ({
  cycleId,
  challengeId,
  challengeName,
  progressCount,
  startTime,
  successCount,
  emojiIndex,
  colorIndex,
}: CertItemProps) => {
  const themeContext = useThemeContext();
  const {
    certEndDate,
    isCertPossible,
    certButtonText,
    handleClickWrapper,
    handleClickButton,
    startTimeString,
  } = useCertItem({
    cycleId,
    challengeId,
    challengeName,
    progressCount,
    startTime,
    successCount,
    emojiIndex,
    colorIndex,
  });

  return (
    <Wrapper
      flexDirection="column"
      gap="1rem"
      onClick={handleClickWrapper}
      style={{ ...cursorPointer }}
      aria-label={`${challengeName} 챌린지 진행중`}
    >
      <TitleWrapper justifyContent="space-between">
        <TitleText
          size={20}
          fontWeight="bold"
          color={themeContext.onBackground}
          aria-label="진행중인 챌린지 이름"
        >
          {challengeName}
        </TitleText>
        <CheckCircles progressCount={progressCount} />
      </TitleWrapper>
      <FlexBox justifyContent="center" gap="1.5rem">
        <Text color={themeContext.onSurface} size={16}>
          해당 챌린지를 총 {successCount}회 성공하셨어요.
        </Text>
      </FlexBox>
      <ChallengeIcon
        emojiIndex={emojiIndex}
        challengeId={challengeId}
        size="large"
        bgColor={colorList[colorIndex]}
      />
      <FlexBox flexDirection="column" justifyContent="center" alignItems="center">
        <Text color={themeContext.mainText} size={16}>
          매일 <Circle>{startTimeString}</Circle>에 시작되는 챌린지
        </Text>
        <Timer certEndDate={certEndDate} />
      </FlexBox>
      <FlexBox justifyContent="center" gap="1.5rem">
        <Button
          disabled={!isCertPossible}
          onClick={handleClickButton}
          size="large"
          aria-label={`${challengeName} 챌린지 ${certButtonText}`}
        >
          {certButtonText}
        </Button>
      </FlexBox>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)`
  ${({ theme }) => css`
    padding: 29px 35px;
    border: 1px solid ${theme.border};
    border-radius: 20px;
    align-items: center;
    width: 100%;
    max-width: 440px;
    min-width: 366px;
    background-color: ${theme.surface};
  `}
`;

const TitleWrapper = styled(FlexBox)`
  width: 96%;
`;

const TitleText = styled(Text)`
  overflow: hidden;
  width: 186px;
  text-overflow: ellipsis;
  white-space: nowrap;
`;

const Circle = styled.em`
  ${({ theme }) => css`
    border-radius: 10px;
    background-color: ${theme.secondary};
    color: ${theme.onSurface};
    padding: 3px 7px;
    font-weight: bold;
    margin-bottom: 6px;
  `}
`;
