import { RefObject } from 'react';

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
export type UseCommentInputProps = CommentInputProps & {
  commentInputRef: RefObject<HTMLDivElement>;
  mentionedMemberIds: number[];
  setMentionedMemberIds: React.Dispatch<React.SetStateAction<number[]>>;
  content: string;
  setContent: (arg0: string) => void;
};
