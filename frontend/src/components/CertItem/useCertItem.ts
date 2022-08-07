import { useCertItemProps } from './type';
import { addDays } from 'utils';

import { CYCLE_UNIT } from 'constants/domain';

const useCertItem = ({ startTime, progressCount }: useCertItemProps) => {
  const nowDate = new Date();
  const certStartDate = addDays(new Date(startTime), progressCount);
  const certEndDate = addDays(new Date(startTime), progressCount + CYCLE_UNIT);

  const isCertPossible = certStartDate <= nowDate && nowDate < certEndDate;
  return { certEndDate, isCertPossible };
};

export default useCertItem;
