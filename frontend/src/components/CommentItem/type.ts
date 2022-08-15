import { Comment } from 'commonType';

export interface CommentItemProps extends Comment {
  isWriter: boolean;
  handleClickMenuButton: (commentId: number) => void;
}
