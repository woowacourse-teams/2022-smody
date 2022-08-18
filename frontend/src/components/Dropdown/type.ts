import { ReactNode } from 'react';

export interface DropdownProps {
  disabled?: boolean;
  button: ReactNode;
  nonLinkableElement?: ReactNode;
  children?: ReactNode;
}
