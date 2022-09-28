import { LoginMember, Member } from 'commonTypes/member';

export type GetTokenGoogleParams = {
  code: string;
};

export type GetTokenGoogleResponse = Pick<LoginMember, 'accessToken'>;

export type GetMyInfoResponse = Omit<Member, 'memberId'>;

export type PatchMyInfoPayload = Pick<Member, 'nickname' | 'introduction'>;

export type PostProfileImagePayload = {
  formData: FormData;
};

export type GetIsValidAccessTokenResponse = {
  isValid: boolean;
};
