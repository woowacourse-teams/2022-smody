import { RefObject } from 'react';

import { ChallengeItemProps } from 'components/ChallengeItem/type';

export interface ChallengeListProps {
  targetRef?: RefObject<HTMLLIElement>;
  challengeListData?: ChallengeItemProps[];
}
