import useInput from 'hooks/useInput';

import { SearchBar } from 'components';

export default {
  title: 'components/SearchBar',
  component: SearchBar,
};

export const DefaultSearchBar = () => {
  const search = useInput('');
  const handleSubmitSearch = () => {};

  return <SearchBar search={search} handleSubmitSearch={handleSubmitSearch} />;
};
