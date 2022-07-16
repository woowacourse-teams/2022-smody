import { getAllChallenges } from 'apis/challengeApi/api';
import { GetChallengeResponse } from 'apis/challengeApi/type';
import { AxiosResponse, AxiosError } from 'axios';
import { useQuery, UseQueryOptions } from 'react-query';

// 5. 모든 챌린지 조회(GET)
export const useGetAllChallenges = (
  options?: UseQueryOptions<AxiosResponse<GetChallengeResponse[]>, AxiosError>,
) =>
  useQuery<AxiosResponse<GetChallengeResponse[]>, AxiosError>(
    'getAllChallenges',
    getAllChallenges,
    options,
  );
