import { ReactNode } from 'react';

export interface DropdownProps {
  disabled?: boolean;
  button: ReactNode;
  nonLinkableElement?: ReactNode;
  updateIsSubscribed: (updatedIsSubscribed: boolean) => void;
  updateNotificationCount: (updatedNotificationCount: number) => void;
}

export type UseDropdownProps = Pick<
  DropdownProps,
  'updateIsSubscribed' | 'updateNotificationCount'
>;
