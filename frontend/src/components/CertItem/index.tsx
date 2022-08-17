import useCertItem from './useCertItem';
import styled, { css } from 'styled-components';
import { parseTime } from 'utils';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text, Button, CheckCircles, Timer, ThumbnailWrapper } from 'components';
import { CertItemProps } from 'components/CertItem/type';

import { colorList, cursorPointer, emojiList } from 'constants/style';

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
    >
      <TitleWrapper justifyContent="space-between">
        <TitleText
          aria-label="진행중인 챌린지 이름"
          size={20}
          fontWeight="bold"
          color={themeContext.onBackground}
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
      <ThumbnailWrapper size="large" bgColor={colorList[colorIndex]}>
        {emojiList[emojiIndex]}
      </ThumbnailWrapper>
      <FlexBox flexDirection="column" justifyContent="center" alignItems="center">
        <Text color={themeContext.mainText} size={16}>
          매일 <Circle>{startTimeString}</Circle>에 시작되는 챌린지
        </Text>
        <Timer certEndDate={certEndDate} />
      </FlexBox>
      <FlexBox justifyContent="center" gap="1.5rem">
        <Button disabled={!isCertPossible} onClick={handleClickButton} size="large">
          {isCertPossible ? '인증하기' : '오늘의 인증 완료🎉'}
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
