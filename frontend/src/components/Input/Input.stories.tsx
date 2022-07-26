import { n } from 'msw/lib/glossary-58eca5a8';
import {
  validateEmail,
  validateIntroduction,
  validateNickname,
  validatePassword,
} from 'utils/validator';

import useInput from 'hooks/useInput';

import { Input } from 'components/Input';

export default {
  title: 'Components/Input',
  component: Input,
  argTypes: {
    icon: {
      table: {
        disable: true,
      },
    },
    type: {
      table: {
        disable: true,
      },
    },
    label: {
      table: {
        disable: true,
      },
    },
    placeholder: {
      table: {
        disable: true,
      },
    },
    value: {
      table: {
        disable: true,
      },
    },
    onChange: {
      table: {
        disable: true,
      },
    },
    isValidated: {
      table: {
        disable: true,
      },
    },
    message: {
      table: {
        disable: true,
      },
    },
  },
};

export const Nickname = () => {
  const nickname = useInput('', validateNickname);

  return (
    <Input type="text" label="닉네임" placeholder="닉네임을 입력해주세요" {...nickname} />
  );
};

export const Introduction = () => {
  const introduction = useInput('', validateIntroduction);

  return (
    <Input
      type="text"
      label="자기 소개"
      placeholder="간단한 자기 소개를 입력해주세요."
      {...introduction}
    />
  );
};
