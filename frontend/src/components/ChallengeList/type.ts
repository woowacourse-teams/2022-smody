import { AxiosResponse } from 'axios';
import { AdditionalChallengeInfo } from 'types/challenge';

export type ChallengeListProps = {
  challengeInfiniteData: AxiosResponse<AdditionalChallengeInfo[]>[];
};
