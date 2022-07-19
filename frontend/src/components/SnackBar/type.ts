export interface SnackBarProps {
  message: string;
  status: 'SUCCESS' | 'ERROR';
  linkText?: string;
  linkTo?: string;
}
