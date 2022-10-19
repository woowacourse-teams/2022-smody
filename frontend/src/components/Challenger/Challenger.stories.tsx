import { ChallengerProps } from './type';

import { Challenger } from 'components';

export default {
  title: 'Components/Challenger',
  component: Challenger,
};

export const DefaultChallenger = (args: ChallengerProps) => <Challenger {...args} />;

DefaultChallenger.args = {
  memberId: 1,
  nickname: '빅터',
  progressCount: 1,
  picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
  introduction: '안녕하세요',
};
