import { ChallengeList } from 'components';
import { ChallengeListProps } from 'components/ChallengeList/type';

export default {
  title: 'Components/ChallengeList',
  component: ChallengeList,
};

export const DefaultChallengeList = (args: ChallengeListProps) => (
  <ChallengeList {...args} />
);

DefaultChallengeList.args = {
  challengeName: '미라클모닝',
  challengerCount: 15,
};
