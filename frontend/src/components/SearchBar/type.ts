import { FormEvent, RefObject } from 'react';

export interface SearchBarProps {
  searchInput: RefObject<HTMLInputElement>;
  handleSubmitSearch: (event: FormEvent<HTMLFormElement>) => void;
  handleChangeSearch: () => void;
}
