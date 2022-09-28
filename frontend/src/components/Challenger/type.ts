import { Cycle } from 'types/cycle';
import { Member } from 'types/member';

export type ChallengerProps = Pick<
  Member,
  'memberId' | 'nickname' | 'introduction' | 'picture'
> &
  Pick<Cycle, 'progressCount'>;
