import { ReactNode } from 'react';

export interface ModalOverlayProps {
  children: ReactNode;
  handleCloseModal(): void;
}
