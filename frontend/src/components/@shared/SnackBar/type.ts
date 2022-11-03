export type SnackBarProps = {
  message: string | null;
  status: 'SUCCESS' | 'ERROR';
  linkText?: string;
  linkTo?: string;
};
