import { ReactNode } from 'react';

export interface BottomSheetProps {
  children: ReactNode;
  handleCloseBottomSheet(): void;
}
