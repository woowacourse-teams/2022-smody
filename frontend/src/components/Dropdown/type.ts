import { ReactNode } from 'react';

interface Item {
  text: string;
  linkTo: string;
}
export interface DropdownProps {
  button: ReactNode;
  children: ReactNode;
  itemList: Item[];
}
