import { Challenge } from './challenge';
import { Cycle, CycleDetail } from './cycle';
import { Member } from './member';

export type Feed = CycleDetail &
  Pick<Member, 'memberId' | 'picture' | 'nickname'> &
  Pick<Cycle, 'challengeId' | 'progressCount'> &
  Pick<Challenge, 'challengeName'> & { commentCount: number };

type CommentInfo = {
  commentId: number;
  content: string;
  createdAt: string;
  isMyComment: boolean;
};

export type Comment = Pick<Member, 'memberId' | 'picture' | 'nickname'> & CommentInfo;
