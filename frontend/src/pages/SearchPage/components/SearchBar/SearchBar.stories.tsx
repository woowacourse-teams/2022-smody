import { SearchBar } from '.';
import { useRef } from 'react';

export default {
  title: 'pages/SearchPage/SearchBar',
  component: SearchBar,
  argTypes: {
    searchInput: {
      table: {
        disable: true,
      },
    },
    handleChangeSearch: {
      table: {
        disable: true,
      },
    },
    handleClickSearchButton: {
      table: {
        disable: true,
      },
    },
  },
};

export const DefaultSearchBar = () => {
  const searchInput = useRef<null | HTMLInputElement>(null);
  const handleChangeSearch = () => {};
  const handleClickSearchButton = () => {};

  return (
    <SearchBar
      searchInput={searchInput}
      handleChangeSearch={handleChangeSearch}
      handleClickSearchButton={handleClickSearchButton}
    />
  );
};
