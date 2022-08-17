import { User } from 'commonType';

export interface GetTokenGoogleResponse {
  accessToken: string;
}

export type GetMyInfoResponse = User;

export type PatchMyInfoProps = Pick<User, 'nickname' | 'introduction'>;

export interface PostProfileImageProps {
  formData: FormData;
}

export interface GetIsValidAccessTokenResponse {
  isValid: boolean;
}
