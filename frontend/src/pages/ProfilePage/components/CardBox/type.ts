import { Challenge, UserChallengeStat } from 'types/challenge';

export type CardBoxProps = Challenge & Pick<UserChallengeStat, 'successCount'>;

export type WrapperProps = { bgColor: string };

export type UseCardBoxProps = Pick<Challenge, 'challengeId'>;
