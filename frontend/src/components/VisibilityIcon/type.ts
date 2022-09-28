import { HTMLInputTypeAttribute, MouseEventHandler } from 'react';

export type VisibilityIconProps = {
  type: HTMLInputTypeAttribute;
  onClick: MouseEventHandler<HTMLDivElement>;
};
