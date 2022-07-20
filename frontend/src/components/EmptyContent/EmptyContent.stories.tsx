import { EmptyContent } from 'components/EmptyContent';
import { EmptyContentProps } from 'components/EmptyContent/type';

export default {
  title: 'Components/EmptyContent',
  component: EmptyContent,
};

export const DefaultEmptyContent = (args: EmptyContentProps) => (
  <EmptyContent {...args} />
);

DefaultEmptyContent.args = {
  title: '빈 페이지입니다 :)',
  description: '아직 준비가 안 끝났어요. 조금만 기다려주세요',
  linkText: '인증 페이지로 돌아가기',
  linkTo: '/cert',
};
