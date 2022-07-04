import EmailIcon from 'assets/email_icon.svg';
import NicknameIcon from 'assets/nickname_icon.svg';
import PersonIcon from 'assets/person_icon.svg';
import PasswordIcon from 'assets/pw_icon.svg';
import { validateEmail, validateNickname, validatePassword } from 'utils/validator';

import useInput from 'hooks/useInput';

import { AuthInput } from 'components/AuthInput';

export default {
  title: 'Components/AuthInput',
  component: AuthInput,
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

export const Email = () => {
  const email = useInput('', validateEmail);

  return (
    <AuthInput
      icon={<EmailIcon />}
      type="email"
      label="이메일"
      placeholder="이메일을 입력해주세요"
      {...email}
    />
  );
};

export const Nickname = () => {
  const nickname = useInput('', validateNickname);

  return (
    <AuthInput
      icon={<PersonIcon />}
      type="text"
      label="닉네임"
      placeholder="닉네임을 입력해주세요"
      {...nickname}
    />
  );
};

export const Password = () => {
  const password = useInput('', validatePassword);

  return (
    <AuthInput
      icon={<PasswordIcon />}
      type="password"
      label="비밀번호"
      placeholder="비밀번호을 입력해주세요"
      {...password}
    />
  );
};
