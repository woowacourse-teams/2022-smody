import { Comment } from 'commonType';

export interface CommentItemProps extends Comment {
  isWriter: boolean;
  isSelectedComment: boolean;
  handleClickMenuButton: (commentId: number) => void;
}

export type WrapperProps = Pick<CommentItemProps, 'isSelectedComment'>;
