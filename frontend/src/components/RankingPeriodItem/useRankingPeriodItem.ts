import { UseRankingPeriodItemProps } from './type';
import { addDays, dateYMDFormatParsing } from 'utils';

import { RANKING_DURATION } from 'constants/domain';

const useRankingPeriodItem = ({ startDate, duration }: UseRankingPeriodItemProps) => {
  const startDateString = dateYMDFormatParsing(startDate);
  const endDateString = dateYMDFormatParsing(
    String(addDays(new Date(Date.parse(startDate)), RANKING_DURATION[duration])),
  );

  return { startDateString, endDateString };
};

export default useRankingPeriodItem;
