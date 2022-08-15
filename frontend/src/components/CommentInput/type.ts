export interface InnerWrapperProps {
  isShowLengthWarning: boolean;
}

export interface WriteButtonProps {
  isVisible: boolean;
}

export interface CommentInputProps {
  editMode: {
    isEditMode: boolean;
    editContent: string;
  };
  turnOffEditMode: () => void;
}
export type UseCommentInputProps = CommentInputProps;
