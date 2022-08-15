import { CustomCycleTimeBottomSheetProps } from './type';
import { useCustomCycleTimeBottomSheet } from './useCustomCycleTimeBottomSheet';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { BottomSheet, Button, Text, FlexBox } from 'components';

const selectTime = [
  '0:00',
  '1:00',
  '2:00',
  '3:00',
  '4:00',
  '5:00',
  '6:00',
  '7:00',
  '8:00',
  '9:00',
  '10:00',
  '11:00',
  '12:00',
  '13:00',
  '14:00',
  '15:00',
  '16:00',
  '17:00',
  '18:00',
  '19:00',
  '20:00',
  '21:00',
  '22:00',
  '23:00',
];

export const CustomCycleTimeBottomSheet = ({
  challengeName,
  startHour,
  joinChallenge,
  handleCloseBottomSheet,
}: CustomCycleTimeBottomSheetProps) => {
  const themeContext = useThemeContext();
  const handleJoinTodayChallenge = useCustomCycleTimeBottomSheet({
    challengeName,
    startHour,
    joinChallenge,
    handleCloseBottomSheet,
  });

  return (
    <BottomSheet handleCloseBottomSheet={handleCloseBottomSheet}>
      <Wrapper flexDirection="column" gap="30px">
        <FlexBox flexDirection="column" gap="10px">
          <Text size={20} fontWeight="bold" color={themeContext.onSurface}>
            {challengeName} 챌린지
          </Text>
          <Text size={20} fontWeight="bold" color={themeContext.onSurface}>
            시작 시간 선택
          </Text>
        </FlexBox>
        <FlexBox flexDirection="column" gap="20px">
          <Text size={16} color={themeContext.onSurface}>
            오늘
          </Text>
          <FlexWrap gap="15px">
            {selectTime
              .filter((_, index) => index >= startHour)
              .map((time, index) => (
                <TimeSelectButton
                  key={index + startHour}
                  onClick={() => handleJoinTodayChallenge(index + startHour)}
                >
                  {time}
                </TimeSelectButton>
              ))}
          </FlexWrap>
          <Text size={16} color={themeContext.onSurface}>
            내일
          </Text>
          <FlexWrap gap="15px">
            {selectTime
              .filter((_, index) => index < startHour)
              .map((time, index) => (
                <TimeSelectButton
                  key={index}
                  onClick={() => handleJoinTodayChallenge(index)}
                >
                  {time}
                </TimeSelectButton>
              ))}
          </FlexWrap>
        </FlexBox>
        <Button size="large" isActive={true} onClick={() => handleJoinTodayChallenge()}>
          지금 바로 도전!!
        </Button>
      </Wrapper>
    </BottomSheet>
  );
};

const Wrapper = styled(FlexBox)`
  max-width: 80%;
  margin: 0 auto;
`;

const FlexWrap = styled(FlexBox)`
  flex-wrap: wrap;
`;

const TimeSelectButton = styled.button`
  ${({ theme }) => css`
    width: 70px;
    height: 35px;
    vertical-align: middle;
    text-align: center;
    font-size: 1rem;
    color: ${theme.onSecondary};
    background-color: ${theme.secondary};
    border-radius: 7px;
    padding: 0.5rem 1rem;
    cursor: pointer;
    &:hover {
      filter: brightness(0.9);
    }
  `}
`;
