import { CommentInput } from '.';
import { CommentInputProps } from './type';

export default {
  title: 'Components/CommentInput',
  component: CommentInput,
  argTypes: {
    editMode: {
      table: {
        disable: true,
      },
    },
    turnOffEditMode: {
      table: {
        disable: true,
      },
    },
  },
};

export const DefaultCommentInput = (args: CommentInputProps) => (
  <CommentInput {...args} />
);

DefaultCommentInput.args = {
  editMode: {
    isEditMode: false,
    editContent: '',
  },
  turnOffEditMode: () => {},
};
