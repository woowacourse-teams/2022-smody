import { AdditionalChallengeInfo } from 'types/challenge';

export type ChallengeItemProps = AdditionalChallengeInfo;

export type useChallengeItemProps = Pick<AdditionalChallengeInfo, 'challengeId'>;
