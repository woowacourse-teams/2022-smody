import { useMentionableInputProps } from 'components/MentionableInput/type';

export type InnerWrapperProps = {
  isShowLengthWarning: boolean;
};

export type WriteButtonProps = {
  isVisible: boolean;
};

export type CommentInputProps = {
  selectedCommentId: number | null;
  editMode: {
    isEditMode: boolean;
    editContent: string;
  };
  turnOffEditMode: () => void;
};
export type UseCommentInputProps = CommentInputProps &
  useMentionableInputProps & { content: string };
