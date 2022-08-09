import { User } from 'commonType';

export interface UserWithdrawalModalProps extends Pick<User, 'email'> {
  handleCloseModal: () => void;
}

export type UseUserWithdrawalModalProps = Pick<UserWithdrawalModalProps, 'email'>;
