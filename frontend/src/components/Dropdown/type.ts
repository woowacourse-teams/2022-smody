import { ReactNode } from 'react';

export interface DropdownProps {
  button: ReactNode;
  nonLinkableElement?: ReactNode;
  children: ReactNode;
}
