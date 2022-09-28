export type BottomSheetProps = {
  height?: string;
  handleCloseBottomSheet(): void;
};

export type BottomSheetContentProps = Pick<BottomSheetProps, 'height'>;
