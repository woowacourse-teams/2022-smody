import { ReactNode } from 'react';

interface Item {
  text: string;
  linkTo: string;
}
export interface DropdownProps {
  children: ReactNode;
  itemList: Item[];
}
