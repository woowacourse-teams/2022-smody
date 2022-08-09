import { RefObject } from 'react';

import { ChallengeItemProps } from 'components/ChallengeItem/type';

export type ChallengeInfo = Omit<ChallengeItemProps, 'challengeListRefetch'>;

export interface ChallengeListProps {
  targetRef?: RefObject<HTMLLIElement>;
  challengeListData?: ChallengeInfo[];
  challengeListRefetch: () => void;
}
