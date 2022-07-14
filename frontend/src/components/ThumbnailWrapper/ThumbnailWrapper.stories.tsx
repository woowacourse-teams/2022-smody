import { ThumbnailWrapper } from 'components/ThumbnailWrapper';
import { ThumbnailWrapperProps } from 'components/ThumbnailWrapper/type';

export default {
  title: 'Components/ThumbnailWrapper',
  component: ThumbnailWrapper,
};

export const DefaultThumbnailWrapper = (args: ThumbnailWrapperProps) => (
  <ThumbnailWrapper {...args}>🎁</ThumbnailWrapper>
);

DefaultThumbnailWrapper.args = {
  size: 'medium',
  bgColor: 'gray',
};
