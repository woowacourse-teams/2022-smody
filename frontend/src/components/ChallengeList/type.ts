import { GetChallengeResponse } from 'apis/challengeApi/type';
import { AxiosResponse } from 'axios';

export interface ChallengeListProps {
  challengeInfiniteData: AxiosResponse<GetChallengeResponse[]>[];
}
