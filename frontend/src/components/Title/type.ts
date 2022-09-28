export type TitleProps = {
  text: string;
  linkTo?: string;
};

export type UseTitleProps = Pick<TitleProps, 'linkTo'>;
