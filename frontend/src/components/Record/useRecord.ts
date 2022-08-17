import { UseRecordProps } from './type';
import { addDays, parseTime } from 'utils';

export const useRecord = ({ startTime, cycleDetails }: UseRecordProps) => {
  const currentCycleCertCount = cycleDetails.length;
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

  return { currentCycleCertCount, cycleProgressTime };
};
