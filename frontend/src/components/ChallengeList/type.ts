import { AxiosResponse } from 'axios';
import { AdditionalChallengeInfo } from 'types/challenge';

export interface ChallengeListProps {
  challengeInfiniteData: AxiosResponse<AdditionalChallengeInfo[]>[];
}
