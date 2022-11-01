import { Title } from 'components/@shared/Title';
import { TitleProps } from 'components/@shared/Title/type';

import { CLIENT_PATH } from 'constants/path';

export default {
  title: '@shared/Title',
  component: Title,
};

export const DefaultTitle = (args: TitleProps) => <Title {...args} />;

DefaultTitle.args = {
  text: '프로필 편집',
  linkTo: CLIENT_PATH.PROFILE_EDIT,
};
