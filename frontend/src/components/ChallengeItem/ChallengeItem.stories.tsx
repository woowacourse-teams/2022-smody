import { ChallengeItem } from 'components';
import { ChallengeItemProps } from 'components/ChallengeItem/type';

export default {
  title: 'Components/ChallengeItem',
  component: ChallengeItem,
};

export const DefaultChallengeItem = (args: ChallengeItemProps) => (
  <ChallengeItem {...args} />
);

DefaultChallengeItem.args = {
  challengeName: '미라클모닝',
  challengerCount: 15,
};
