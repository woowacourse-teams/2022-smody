import { usePostCycleProgress } from 'apis';
import { MouseEvent, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled, { css } from 'styled-components';
import { addDays } from 'utils';
import { getEmoji } from 'utils/emoji';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text, Button, CheckCircles, Timer, ThumbnailWrapper } from 'components';
import { CertItemProps } from 'components/CertItem/type';
import { SuccessModal } from 'components/SuccessModal';

import { CYCLE_UNIT } from 'constants/domain';
import { CLIENT_PATH } from 'constants/path';
import { cursorPointer } from 'constants/style';

export const CertItem = ({
  cycleId,
  challengeId,
  challengeName,
  progressCount,
  startTime,
  successCount,
  refetch,
}: CertItemProps) => {
  const themeContext = useThemeContext();
  const navigate = useNavigate();

  const nowDate = new Date();
  const certStartDate = addDays(new Date(startTime), progressCount);
  const certEndDate = addDays(new Date(startTime), progressCount + CYCLE_UNIT);

  const isCertPossible = certStartDate <= nowDate && nowDate < certEndDate;

  const [isSuccessModalOpen, setIsSuccessModalOpen] = useState(false);

  const { mutate } = usePostCycleProgress({
    onSuccess: () => {
      setIsSuccessModalOpen(true);
    },
  });

  const handleClickWrapper = (e: MouseEvent) => {
    if (e.currentTarget !== e.target) {
      return;
    }
    navigate(`${CLIENT_PATH.CYCLE_DETAIL}/${cycleId}`);
  };

  const handleClickButton = () => {
    mutate({ cycleId });
  };

  const handleCloseModal = () => {
    setIsSuccessModalOpen(false);
    refetch();
  };

  return (
    <Wrapper
      flexDirection="column"
      gap="1rem"
      onClick={(e) => handleClickWrapper(e)}
      style={{ ...cursorPointer }}
    >
      <TitleWrapper justifyContent="space-between">
        <TitleText size={20} fontWeight="bold" color={themeContext.onBackground}>
          {challengeName}
        </TitleText>
        <CheckCircles progressCount={progressCount} />
      </TitleWrapper>
      <RowWrapper justifyContent="center" gap="1.5rem">
        <Text color={themeContext.onSurface} size={16}>
          해당 챌린지를 총 {successCount}회 성공하셨어요.
        </Text>
      </RowWrapper>
      <ThumbnailWrapper size="large" bgColor="transparent">
        <p>{getEmoji(Number(challengeId))}</p>
      </ThumbnailWrapper>
      <RowWrapper justifyContent="center" gap="1.5rem">
        <Timer certEndDate={certEndDate} />
      </RowWrapper>
      <RowWrapper justifyContent="center" gap="1.5rem">
        <Button disabled={!isCertPossible} onClick={handleClickButton} size="large">
          {isCertPossible ? '인증하기' : '오늘의 인증 완료🎉'}
        </Button>
      </RowWrapper>
      {isSuccessModalOpen && (
        <SuccessModal
          handleCloseModal={handleCloseModal}
          challengeId={challengeId}
          challengeName={challengeName}
          successCount={successCount}
          progressCount={progressCount + 1}
        />
      )}
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

const RowWrapper = styled(FlexBox)`
  width: 100%;
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
