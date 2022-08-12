import { FormEvent } from 'react';

import useInput from 'hooks/useInput';

export interface SearchBarProps {
  search: ReturnType<typeof useInput>;
  handleSubmitSearch: (event: FormEvent<HTMLFormElement>) => void;
}
