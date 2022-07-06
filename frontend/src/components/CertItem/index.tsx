import { Cycle } from 'commonType';
import { useContext } from 'react';
import styled, { css, ThemeContext } from 'styled-components';
import { addDays, parseTime } from 'utils';

import { FlexBox, Text, Button, CheckCircles, Timer, UnderLineText } from 'components';

const cycleUnit = 1;

export const CertItem = ({
  cycleId,
  challengeId,
  challengeName,
  progressCount,
  startTime,
  successCount,
}: Cycle) => {
  const themeContext = useContext(ThemeContext);

  const nowDate = new Date();
  const certStartDate = addDays(new Date(startTime), progressCount);
  const certEndDate = addDays(new Date(startTime), progressCount + cycleUnit);
  const parsedStartTime = parseTime(new Date(startTime));
  const isCertPossible = certStartDate <= nowDate && nowDate < certEndDate;

  const handleClick = () => {
    alert('hi');
  };

  return (
    <Wrapper>
      <RowWrapper>
        <UnderLineText
          fontSize={20}
          fontColor={themeContext.onSurface}
          underLineColor={themeContext.primary}
          fontWeight="bold"
        >
          {challengeName}
        </UnderLineText>
        <CheckCircles progressCount={progressCount} />
      </RowWrapper>
      <RowWrapper>
        <Timer certEndDate={certEndDate} />
        <Button disabled={!isCertPossible} onClick={handleClick} size="medium">
          {isCertPossible ? '인증하기' : '오늘의 인증 완료🎉'}
        </Button>
      </RowWrapper>
      <RowWrapper>
        <Text color={themeContext.onBackground} size={20}>
          {parsedStartTime}부터 도전중🔥
        </Text>
        <Text color={themeContext.onBackground} size={20} fontWeight="bold">
          총 {successCount}회 성공
        </Text>
      </RowWrapper>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  gap: '1rem',
})`
  ${({ theme }) => css`
    padding: 1rem;
    border: 1px solid ${theme.border};
    border-radius: 10px;
    align-items: center;
    min-width: 330px;
    background-color: ${theme.surface};
  `}
`;

const RowWrapper = styled(FlexBox).attrs({
  alignItems: 'center',
  gap: '1rem',
})``;
