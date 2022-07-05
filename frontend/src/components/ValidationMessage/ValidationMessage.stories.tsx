import { ValidationMessage } from 'components/ValidationMessage';
import { ValidationMessageProps } from 'components/ValidationMessage/type';

export default {
  title: 'components/ValidationMessage',
  component: ValidationMessage,
  argsTypes: {
    value: {
      table: {
        disable: true,
      },
    },
  },
};

export const ValidMessage = (args: ValidationMessageProps) => (
  <ValidationMessage {...args} value="value" />
);

ValidMessage.args = {
  isValidated: true,
  message: '유효한 메시지',
};

export const InvalidMessage = (args: ValidationMessageProps) => (
  <ValidationMessage {...args} value="value" />
);

InvalidMessage.args = {
  isValidated: false,
  message: '유효하지 않은 메시지',
};
