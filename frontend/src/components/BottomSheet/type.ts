import { ReactNode } from 'react';

export interface BottomSheetProps {
  children: ReactNode;
  height?: string;
  handleCloseBottomSheet(): void;
}

export type BottomSheetContentProps = Pick<BottomSheetProps, 'height'>;
