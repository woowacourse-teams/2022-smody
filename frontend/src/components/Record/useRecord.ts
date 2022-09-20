import { UseRecordProps } from './type';
import { useNavigate } from 'react-router-dom';
import { addDays, parseTime } from 'utils';

import { CYCLE_SUCCESS_CRITERIA } from 'constants/domain';
import { CLIENT_PATH } from 'constants/path';

export const useRecord = ({ cycleId, startTime, cycleDetails }: UseRecordProps) => {
  const navigate = useNavigate();

  const currentCycleCertCount = cycleDetails.length;
  const isSuccess = currentCycleCertCount === CYCLE_SUCCESS_CRITERIA;

  const cycleStartDate = new Date(startTime);
  const {
    year: endYear,
    month: endMonth,
    date: endDate,
  } = parseTime(String(addDays(cycleStartDate, 2)));

  const {
    year: startYear,
    month: startMonth,
    date: startDate,
  } = parseTime(String(cycleStartDate));

  const cycleProgressTime = `${startYear}.${startMonth}.${startDate} ~ ${endYear}.${endMonth}.${endDate}`;

  const handleClickShare = () => {
    navigate(`${CLIENT_PATH.CYCLE_DETAIL_SHARE}/${cycleId}`);
  };

  return { currentCycleCertCount, isSuccess, cycleProgressTime, handleClickShare };
};
