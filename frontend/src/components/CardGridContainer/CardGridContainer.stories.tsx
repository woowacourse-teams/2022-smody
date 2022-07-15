import { CardGridContainer } from 'components';

export default {
  title: 'components/CardGridContainer',
  component: CardGridContainer,
};

export const DefaultCardGridContainer = () => <CardGridContainer />;

DefaultCardGridContainer.args = {
  successChallenges: [
    {
      challengeId: 1,
      challengeName: '헬스장 가기',
      successCount: 15,
      bgColor: 'red',
      emoji: '💪',
    },
    {
      challengeId: 2,
      challengeName: '독서 모임',
      successCount: 20,
      bgColor: 'blue',
      emoji: '💪',
    },
    {
      challengeId: 3,
      challengeName: '미라클 모닝',
      successCount: 11,
      bgColor: 'pink',
      emoji: '💪',
    },
    {
      challengeId: 4,
      challengeName: '헬스장 가기',
      successCount: 15,
      bgColor: 'brown',
      emoji: '💪',
    },
    {
      challengeId: 5,
      challengeName: '헬스장 가기',
      successCount: 15,
      bgColor: 'yellow',
      emoji: '💪',
    },
    {
      challengeId: 6,
      challengeName: '헬스장 가기',
      successCount: 15,
      bgColor: 'orange',
      emoji: '💪',
    },
  ],
};
