import { AdditionalChallengeInfo, Challenge } from 'types/challenge';

export type EventChallengeItemProps = Pick<Challenge, 'challengeId' | 'challengeName'> & {
  link: string;
};

export type useEventChallengeItemProps = Pick<AdditionalChallengeInfo, 'challengeId'>;
