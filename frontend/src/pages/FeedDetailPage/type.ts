export type MenuBottomSheetProps = {
  isLoadingDeleteComment: boolean;
  handleCloseBottomSheet: () => void;
  handleClickCommentEdit: () => void;
  handleClickCommentDelete: () => void;
};

export type SelectedCommentIdType = number | null;
