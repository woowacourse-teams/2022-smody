import {
  RecordProps,
  RecordItemProps,
  RecordItemWrapperProps,
  RecordWrapperProps,
} from './type';
import { useRecord } from './useRecord';
import { useRecordItem } from './useRecordItem';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { Button, FlexBox, Text, CheckSuccessCycle, ChallengeIcon } from 'components';

import { CYCLE_SUCCESS_CRITERIA } from 'constants/domain';
import { cursorPointer, emojiList } from 'constants/style';

const CyclePeriod = [...Array(CYCLE_SUCCESS_CRITERIA)];

export const Record = ({ cycleId, emojiIndex, startTime, cycleDetails }: RecordProps) => {
  const themeContext = useThemeContext();
  const { currentCycleCertCount, isSuccess, cycleProgressTime, handleClickShare } =
    useRecord({
      cycleId,
      startTime,
      cycleDetails,
    });

  return (
    <RecordWrapper flexDirection="column" gap="10px" isSuccess={isSuccess}>
      <FlexBox justifyContent="space-between">
        <FlexBox>
          <CycleProgressTime color={themeContext.onSurface} fontWeight="bold">
            {cycleProgressTime}
          </CycleProgressTime>
          <CheckSuccessCycle isSuccess={isSuccess} />
        </FlexBox>
        {isSuccess && (
          <Button onClick={handleClickShare} size="small">
            공유하기
          </Button>
        )}
      </FlexBox>
      <FlexBox justifyContent="space-between" gap="10px">
        {CyclePeriod.map((_, index) => (
          <RecordItem
            key={index}
            cycleDetails={cycleDetails}
            emojiIndex={emojiIndex}
            isBlank={currentCycleCertCount < index + 1}
            index={index}
          />
        ))}
      </FlexBox>
    </RecordWrapper>
  );
};

const RecordItem = ({ cycleDetails, emojiIndex, isBlank, index }: RecordItemProps) => {
  const handleNavigateFeedDetail = useRecordItem();

  if (isBlank) {
    return (
      <RecordItemWrapper justifyContent="center" alignItems="center">
        <ChallengeIcon size="medium" bgColor="transparent">
          {emojiList[emojiIndex]}
        </ChallengeIcon>
      </RecordItemWrapper>
    );
  }

  return (
    <RecordItemWrapper
      style={{ ...cursorPointer }}
      recordImg={cycleDetails[index].progressImage}
      onClick={() =>
        handleNavigateFeedDetail({ cycleDetailId: cycleDetails[index].cycleDetailId })
      }
    />
  );
};

const RecordWrapper = styled(FlexBox)<RecordWrapperProps>`
  ${({ theme, isSuccess }) => css`
    max-width: 1000px;
    border-radius: 20px;
    padding: 10px 15px 15px;
    margin: 0 auto;
    background-color: ${theme.secondary};

    ${isSuccess &&
    css`
      border: 3px solid ${theme.successBorder};
    `};
  `}
`;

const CycleProgressTime = styled(Text)`
  margin: 0 0.4rem;
  line-height: 1.8;
  flex-grow: 1;
`;

const RecordItemWrapper = styled(FlexBox)<RecordItemWrapperProps>`
  ${({ theme, recordImg }) => css`
    position: relative;
    width: 30%;
    border-radius: 20px;
    background-color: ${theme.surface};
    ${recordImg &&
    css`
      background: url(${recordImg}) no-repeat center / cover;
      transition: all 0.2s linear;
      opacity: 0.8;
      &:hover {
        opacity: 1;
      }
    `};

    &:after {
      content: '';
      display: block;
      padding-bottom: 100%;
    }
  `}
`;
