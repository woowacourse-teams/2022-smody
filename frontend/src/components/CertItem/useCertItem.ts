import { CertItemProps } from './type';
import { MouseEventHandler } from 'react';
import { useNavigate } from 'react-router-dom';
import { addDays } from 'utils';

import { CYCLE_UNIT } from 'constants/domain';
import { CLIENT_PATH } from 'constants/path';

const useCertItem = ({
  cycleId,
  challengeId,
  challengeName,
  progressCount,
  startTime,
  successCount,
}: CertItemProps) => {
  const navigate = useNavigate();
  const nowDate = new Date();
  const certStartDate = addDays(new Date(startTime), progressCount);
  const certEndDate = addDays(new Date(startTime), progressCount + CYCLE_UNIT);

  const isCertPossible = certStartDate <= nowDate && nowDate < certEndDate;

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
      },
    });
  };

  return { certEndDate, isCertPossible, handleClickWrapper, handleClickButton };
};

export default useCertItem;
