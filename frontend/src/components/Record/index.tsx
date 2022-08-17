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

import { FlexBox, Text } from 'components';

import { CYCLE_SUCCESS_CRITERIA } from 'constants/domain';
import { cursorPointer, emojiList } from 'constants/style';

const CyclePeriod = [...Array(CYCLE_SUCCESS_CRITERIA)];

export const Record = ({ emojiIndex, startTime, cycleDetails }: RecordProps) => {
  const themeContext = useThemeContext();
  const { currentCycleCertCount, cycleProgressTime } = useRecord({
    startTime,
    cycleDetails,
  });

  return (
    <RecordWrapper
      flexDirection="column"
      gap="10px"
      isSuccess={currentCycleCertCount === CYCLE_SUCCESS_CRITERIA}
    >
      <Text color={themeContext.onSurface} fontWeight="bold">
        {cycleProgressTime}
      </Text>
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
  const themeContext = useThemeContext();
  const handleNavigateFeedDetail = useRecordItem();

  if (isBlank) {
    return (
      <RecordItemWrapper justifyContent="center" alignItems="center">
        <Text size={32} color={themeContext.onSurface}>
          {emojiList[emojiIndex]}
        </Text>
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
    background-color: ${isSuccess ? theme.secondary : theme.blur};
  `}
`;

const RecordItemWrapper = styled(FlexBox)<RecordItemWrapperProps>`
  ${({ theme, recordImg }) => css`
    position: relative;
    width: 30%;
    border-radius: 20px;
    background-color: ${theme.disabled};
    ${recordImg &&
    css`
      background: url(${recordImg}) no-repeat center / cover;
    `};

    &:after {
      content: '';
      display: block;
      padding-bottom: 100%;
    }
  `}
`;
