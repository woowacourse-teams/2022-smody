import { RefObject } from 'react';

export type SearchBarProps = {
  searchInput: RefObject<HTMLInputElement>;
  handleChangeSearch: () => void;
  handleClickSearchButton: () => void;
};
