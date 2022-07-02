import COLOR from 'styles/color';

export type AvailablePickedColor = typeof COLOR extends { [key: string]: infer T }
  ? T
  : never;
