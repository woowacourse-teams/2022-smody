import { CertItemProps } from './type';
import { MouseEventHandler } from 'react';
import { useNavigate } from 'react-router-dom';
import { addDays, parseTime } from 'utils';

import { CYCLE_UNIT } from 'constants/domain';
import { CLIENT_PATH } from 'constants/path';

const useCertItem = ({
  cycleId,
  challengeId,
  challengeName,
  progressCount,
  startTime,
  successCount,
  emojiIndex,
  colorIndex,
}: CertItemProps) => {
  const navigate = useNavigate();
  const nowDate = new Date();
  const certStartDate = addDays(new Date(startTime), progressCount);
  const certEndDate = addDays(new Date(startTime), progressCount + CYCLE_UNIT);

  const isCertPossible = certStartDate <= nowDate && nowDate < certEndDate;
  const certButtonText = isCertPossible ? '인증하기' : '오늘의 인증 완료🎉';

  const handleClickWrapper: MouseEventHandler<HTMLDivElement> = (e) => {
    if (e.target instanceof HTMLButtonElement) {
      return;
    }

    navigate(`${CLIENT_PATH.CYCLE_DETAIL}/${cycleId}`);
  };

  const handleClickButton = () => {
    navigate(CLIENT_PATH.CERT, {
      state: {
        isInCertFormPage: true,
        cycleId,
        challengeId,
        challengeName,
        progressCount,
        successCount,
        emojiIndex,
        colorIndex,
      },
    });
  };

  const { hours, minutes } = parseTime(startTime);
  const startTimeString = `${hours}:${minutes}`;

  return {
    certEndDate,
    isCertPossible,
    certButtonText,
    handleClickWrapper,
    handleClickButton,
    startTimeString,
  };
};

export default useCertItem;
