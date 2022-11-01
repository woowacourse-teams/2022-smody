import { AvailablePickedColor } from 'types/style';

export type TitleProps = {
  text: string;
  linkTo?: string;
};

export type UseTitleProps = Pick<TitleProps, 'linkTo'>;

export type TitleWrapperProps = {
  bgColor: AvailablePickedColor;
};
