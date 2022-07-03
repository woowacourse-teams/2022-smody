import EmailIcon from 'assets/email_icon.svg';

// import NicknameIcon from 'assets/nickname_icon.svg';
// import PasswordIcon from 'assets/pw_icon.svg';
import { Input } from 'components/Input';

console.log(EmailIcon);
export default {
  title: 'Components/Input',
  component: Input,
};

const Template = (args) => <Input {...args} />;

export const EmailTemplate = Template.bind({});
EmailTemplate.args = {
  label: 'Email Address',
  icon: <EmailIcon />,
  type: 'email',
  placeholder: '이메일을 입력해주세요',
};

export const NicknameTemplate = Template.bind({});
NicknameTemplate.args = {
  label: 'Nickname',
  // icon: <NicknameIcon />,
  type: 'text',
  placeholder: '닉네임을 입력해주세요',
};

export const PasswordTemplate = Template.bind({});
PasswordTemplate.args = {
  label: 'Password',
  // icon: <PasswordIcon />,
  type: 'password',
  placeholder: '비밀번호를 입력해주세요',
};
