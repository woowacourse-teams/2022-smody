import { Feed } from 'types/feed';

import { FeedItem } from 'components';

export default {
  title: 'Components/FeedItem',
  component: FeedItem,
};

export const DefaultFeedItem = (args: Feed) => <FeedItem {...args} />;

DefaultFeedItem.args = {
  cycleDetailId: 2,
  picture: 'https://emoji-copy.com/wp-content/uploads/1f64a.png',
  nickname: '테스트닉네임',
  progressImage:
    'https://cdn.pixabay.com/photo/2019/07/21/13/42/nature-conservation-4352793_1280.jpg',
  progressCount: 2,
  progressTime: '2022-08-08T10:00:00',
  description: '오늘 날씨가 너무 좋아요~!',
  challengeId: 1,
  challengeName: '미라클 모닝',
  commentCount: 4,
};
