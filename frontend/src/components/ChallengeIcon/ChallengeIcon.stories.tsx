import { ChallengeIcon } from 'components/ChallengeIcon';
import { ChallengeIconProps } from 'components/ChallengeIcon/type';

export default {
  title: 'components/ChallengeIcon',
  component: ChallengeIcon,
};

export const DefaultChallengeIcon = (args: ChallengeIconProps) => (
  <ChallengeIcon {...args}>🎁</ChallengeIcon>
);

ChallengeIcon.args = {
  size: 'medium',
  bgColor: 'gray',
};
