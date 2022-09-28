import { Comment } from 'types/feed';

export type CommentItemProps = Comment & {
  isWriter: boolean;
  isSelectedComment: boolean;
  handleClickMenuButton: (commentId: number) => void;
};

export type WrapperProps = Pick<CommentItemProps, 'isSelectedComment'>;
