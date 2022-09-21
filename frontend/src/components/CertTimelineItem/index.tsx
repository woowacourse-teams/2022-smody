import { CertTimelineItemProps, ItemWrapperProps } from './type';
import styled, { css } from 'styled-components';

import { Button } from 'components/@shared/Button';
import { FlexBox } from 'components/@shared/FlexBox';

import useCertItem from 'components/CertItem/useCertItem';
import { CheckCircles } from 'components/CheckCircles';

import { cursorPointer, emojiList } from 'constants/style';

export const CertTimelineItem = ({ cycleInfo, time }: CertTimelineItemProps) => {
  const {
    cycleId,
    challengeId,
    challengeName,
    colorIndex,
    emojiIndex,
    progressCount,
    startTime,
    successCount,
  } = cycleInfo;

  const { isCertPossible, handleClickWrapper, handleClickButton, startTimeString } =
    useCertItem({
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
    <ItemWrapper
      key={cycleId}
      time={time}
      onClick={handleClickWrapper}
      style={{ ...cursorPointer }}
    >
      <MainContents>
        <div>{startTimeString}</div>
        {emojiList[emojiIndex]} {challengeName}
      </MainContents>

      <SubContents>
        <CheckCircles progressCount={progressCount} gap="0.5rem" />
        <Button
          disabled={!isCertPossible}
          onClick={handleClickButton}
          size="small"
          style={{ width: '80px' }}
        >
          {isCertPossible ? '인증하기' : '인증완료'}
        </Button>
      </SubContents>
    </ItemWrapper>
  );
};

const ItemWrapper = styled.div<ItemWrapperProps>`
  ${({ theme, time }) => css`
    background-color: ${time === 'now' && theme.accent};
    color: ${time === 'now' && theme.onAccent};

    display: grid;
    grid-template-columns: 1fr 1fr;
    align-items: center;
    width: 100%;
    padding: 0.8rem;
    border-bottom: 1px solid ${theme.primary};

    &:last-child {
      border-bottom: none;
    }
  `}
`;

const MainContents = styled(FlexBox)`
  gap: 1rem;
  line-height: 1.4;
  align-items: center;
`;

const SubContents = styled(FlexBox)`
  justify-content: flex-end;
  gap: 1rem;
`;
