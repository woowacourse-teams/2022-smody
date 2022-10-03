import { atom } from 'recoil';

import { INIT_RANKING_PERIOD_ID } from 'constants/domain';

export const selectedRankingPeriodIdState = atom({
  key: 'selectedRankingPeriodIdState',
  default: INIT_RANKING_PERIOD_ID,
});
