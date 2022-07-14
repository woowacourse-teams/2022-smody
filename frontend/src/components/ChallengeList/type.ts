import { ChallengeItemProps } from 'components/ChallengeItem/type';

export type ChallengeInfo = Omit<ChallengeItemProps, 'challengeListRefetch'>;
