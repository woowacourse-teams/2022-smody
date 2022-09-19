import { useRef } from 'react';

import { SearchBar } from 'components';

export default {
  title: 'components/SearchBar',
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
