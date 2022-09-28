export interface TitleProps {
  text: string;
  linkTo?: string;
}

export type UseTitleProps = Pick<TitleProps, 'linkTo'>;
