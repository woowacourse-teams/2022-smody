import { Member } from 'types/member';

export type UserWithdrawalModalProps = Pick<Member, 'email'> & {
  handleCloseModal: () => void;
};

export type UseUserWithdrawalModalProps = Pick<UserWithdrawalModalProps, 'email'>;
