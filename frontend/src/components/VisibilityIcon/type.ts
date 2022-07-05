import { HTMLInputTypeAttribute, MouseEventHandler } from 'react';

export interface VisibilityIconProps {
  type: HTMLInputTypeAttribute;
  onClick: MouseEventHandler<HTMLDivElement>;
}
