import { RefObject } from 'react';

export interface SearchBarProps {
  searchInput: RefObject<HTMLInputElement>;
  handleChangeSearch: () => void;
  handleClickSearchButton: () => void;
}
