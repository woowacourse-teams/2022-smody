export interface AuthProps {
  email: string;
  password: string;
  nickname: string;
}

export type LoginProps = Pick<AuthProps, 'email' | 'password'>;

export interface PostSignUpResponse {
  email: string;
}

export interface PostLoginResponse {
  accessToken: string;
  nickname: string;
}
