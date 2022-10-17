import { ChallengeIcon } from 'components/ChallengeIcon';
import { ChallengeIconProps } from 'components/ChallengeIcon/type';

export default {
  title: 'components/ChallengeIcon',
  component: ChallengeIcon,
};

export const DefaultChallengeIcon = (args: ChallengeIconProps) => (
  <ChallengeIcon {...args} />
);

DefaultChallengeIcon.args = {
  size: 'medium',
  bgColor: 'gray',
  emojiIndex: '1',
};
