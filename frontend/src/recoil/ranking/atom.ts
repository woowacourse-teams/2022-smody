import { atom } from 'recoil';

import { INIT_MY_MEMBER_ID, INIT_RANKING_PERIOD_ID } from 'constants/domain';

export const selectedRankingPeriodIdState = atom({
  key: 'selectedRankingPeriodIdState',
  default: INIT_RANKING_PERIOD_ID,
});

export const myMemberIdState = atom({
  key: 'myMemberIdState',
  default: INIT_MY_MEMBER_ID,
});
