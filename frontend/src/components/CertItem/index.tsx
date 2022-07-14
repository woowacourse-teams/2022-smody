import { useContext } from 'react';
import styled, { css, ThemeContext } from 'styled-components';
import { addDays } from 'utils';

import { FlexBox, Text, Button, CheckCircles, Timer, ThumbnailWrapper } from 'components';
import { CertItemProps } from 'components/CertItem/type';

const cycleUnit = 1;

export const CertItem = ({
  cycleId,
  challengeName,
  progressCount,
  startTime,
  successCount,
  handleClickCertification,
}: CertItemProps) => {
  const themeContext = useContext(ThemeContext);

  const nowDate = new Date();
  const certStartDate = addDays(new Date(startTime), progressCount);
  const certEndDate = addDays(new Date(startTime), progressCount + cycleUnit);

  const isCertPossible = certStartDate <= nowDate && nowDate < certEndDate;

  const handleClick = () => {
    handleClickCertification(cycleId);
  };

  return (
    <Wrapper>
      <TitleWrapper>
        <TitleText size={24} fontWeight="bold" color={themeContext.onBackground}>
          {challengeName}
        </TitleText>
        <CheckCircles progressCount={progressCount} />
      </TitleWrapper>
      <RowWrapper>
        <Text color={themeContext.onSurface} size={16}>
          해당 챌린지를 총 {successCount}회 성공하셨어요.
        </Text>
      </RowWrapper>
      <ThumbnailWrapper size="large" bgColor="transparent">
        🌞
      </ThumbnailWrapper>
      <RowWrapper>
        <Timer certEndDate={certEndDate} />
      </RowWrapper>
      <RowWrapper>
        <Button disabled={!isCertPossible} onClick={handleClick} size="large">
          {isCertPossible ? '인증하기' : '오늘의 인증 완료🎉'}
        </Button>
      </RowWrapper>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  gap: '1rem',
})`
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

const RowWrapper = styled(FlexBox).attrs({
  justifyContent: 'center',
  gap: '1.5rem',
})`
  width: 100%;
`;

const TitleWrapper = styled(FlexBox).attrs({
  justifyContent: 'space-between',
})`
  width: 96%;
`;

const EmojiWrapper = styled(FlexBox).attrs({
  justifyContent: 'center',
})`
  padding: 14px 0;
`;

const TitleText = styled(Text)`
  overflow: hidden;
  width: 186px;
  text-overflow: ellipsis;
  white-space: nowrap;
`;
