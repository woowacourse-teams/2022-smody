import { CardBox } from '.';
import { CardBoxProps } from './type';

export default {
  title: 'pages/ProfilePage/CardBox',
  component: CardBox,
};

export const DefaultCardBox = (args: CardBoxProps) => <CardBox {...args} />;

DefaultCardBox.args = {
  bgColor: 'pink',
  challengeName: '헬스장 가기',
  successCount: 15,
  emojiIndex: 1,
  colorIndex: 1,
};
