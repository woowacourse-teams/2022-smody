import { ReactNode } from 'react';

export interface TitleProps {
  text: string;
  linkTo?: string;
  children?: ReactNode;
}

export type UseTitleProps = Pick<TitleProps, 'linkTo'>;
