export type Member = {
  memberId: number;
  email: string;
  nickname: string;
  introduction: string;
  picture: string;
};

export type LoginMember = {
  accessToken: string;
  isNewMember: boolean;
};
