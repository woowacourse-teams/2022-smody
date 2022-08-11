import { CommentItemProps } from './type';

import { CommentItem } from 'components/CommentItem';

export default {
  title: 'Components/CommentItem',
  component: CommentItem,
  argTypes: {
    commentId: {
      table: {
        disable: true,
      },
    },
    memberId: {
      table: {
        disable: true,
      },
    },
    createdAt: {
      table: {
        disable: true,
      },
    },
  },
};

export const DefaultCommentItem = (args: CommentItemProps) => <CommentItem {...args} />;

DefaultCommentItem.args = {
  commentId: 1,
  memberId: 1,
  nickname: '테스트닉네임',
  picture: 'https://emoji-copy.com/wp-content/uploads/1f64a.png',
  content: '오!!! 정말 열심히 하셨네요ㅎㅎ 저도 좋은 자극 받고 갑니당ㅎㅎ',
  createdAt: '2022-08-08T10:00:00',
  isWriter: true,
};
