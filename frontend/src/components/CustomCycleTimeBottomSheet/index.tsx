import { CustomCycleTimeBottomSheetProps } from './type';
import { useCustomCycleTimeBottomSheet } from './useCustomCycleTimeBottomSheet';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { BottomSheet, Button, Text, FlexBox, UnderLineText } from 'components';

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
  joinChallenge,
  handleCloseBottomSheet,
}: CustomCycleTimeBottomSheetProps) => {
  const themeContext = useThemeContext();
  const { startHour, selectTimeIndex, handleSelectTime, handleJoinTodayChallenge } =
    useCustomCycleTimeBottomSheet({
      challengeName,
      joinChallenge,
      handleCloseBottomSheet,
    });

  return (
    <BottomSheet handleCloseBottomSheet={handleCloseBottomSheet}>
      <Wrapper flexDirection="column" gap="10px">
        <FlexBox flexDirection="column" gap="10px">
          <Text size={20} fontWeight="bold" color={themeContext.onSurface}>
            시작 시간 선택
          </Text>
          <TipText color={themeContext.mainText}>
            시작 시간 이후 24시간 이내에 챌린지를 완료해주세요.
            <br />
            3일 동안 매일 해당 시작 시간에 챌린지가 재개돼요.
          </TipText>
          <StartInfoWrapper>
            <div>
              <UnderLineText
                fontSize={20}
                fontWeight="bold"
                fontColor={themeContext.onSurface}
                underLineColor={themeContext.primary}
              >
                {challengeName}
              </UnderLineText>
              <Text size={20} fontWeight="bold" color={themeContext.onSurface}>
                &nbsp;챌린지&nbsp;
              </Text>
            </div>
            <FlexBox alignItems="center">
              <div>
                <Text size={20} fontWeight="bold" color={themeContext.primary}>
                  {selectTimeIndex < startHour ? '내일' : '오늘'}{' '}
                  {selectTime[selectTimeIndex]}
                  &nbsp;
                </Text>
                <Text size={20} fontWeight="bold" color={themeContext.onSurface}>
                  부터&nbsp;
                </Text>
              </div>
              <Button size="small" onClick={handleJoinTodayChallenge}>
                시작
              </Button>
            </FlexBox>
          </StartInfoWrapper>
        </FlexBox>
        <FlexBox flexDirection="column" gap="20px">
          <Text size={16} color={themeContext.onSurface}>
            오늘
          </Text>
          <FlexWrap gap="15px">
            {selectTime
              .filter((_, index) => index >= startHour)
              .map((time, index) => (
                <div key={index + startHour}>
                  <SelectTimeInputRadio
                    id={String(index + startHour)}
                    value={index + startHour}
                    checked={selectTimeIndex === index + startHour}
                    onChange={handleSelectTime}
                  />
                  <label htmlFor={String(index + startHour)}>
                    <TimeSelectButton as="div">{time}</TimeSelectButton>
                  </label>
                </div>
              ))}
          </FlexWrap>
          <Text size={16} color={themeContext.onSurface}>
            내일
          </Text>
          <FlexWrap gap="15px">
            {selectTime
              .filter((_, index) => index < startHour)
              .map((time, index) => (
                <div key={index}>
                  <SelectTimeInputRadio
                    id={String(index)}
                    value={index}
                    checked={selectTimeIndex === index}
                    onChange={handleSelectTime}
                  />
                  <label htmlFor={String(index)}>
                    <TimeSelectButton as="div">{time}</TimeSelectButton>
                  </label>
                </div>
              ))}
          </FlexWrap>
        </FlexBox>
      </Wrapper>
    </BottomSheet>
  );
};

const Wrapper = styled(FlexBox)`
  min-width: 220px;
  max-width: 80%;
  margin: 0 auto;
`;

const FlexWrap = styled(FlexBox)`
  flex-wrap: wrap;
`;

const TipText = styled(Text)`
  line-height: 1.5rem;
`;

const StartInfoWrapper = styled.div`
  div,
  ${Text}, ${Button}, ${UnderLineText} {
    display: inline-block;
    height: 34px;
    line-height: 34px;
    vertical-align: bottom;
  }
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

const SelectTimeInputRadio = styled.input.attrs({
  type: 'radio',
  name: 'selectTimeList',
})`
  display: none;

  ${({ theme }) => css`
    &:checked + label ${TimeSelectButton} {
      color: ${theme.onPrimary};
      background-color: ${theme.primary};
    }
  `}
`;
