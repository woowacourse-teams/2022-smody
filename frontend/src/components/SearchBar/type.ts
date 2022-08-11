import { FormEventHandler } from 'react';

import useInput from 'hooks/useInput';

export interface SearchBarProps {
  search: ReturnType<typeof useInput>;
  handleSubmitSearch: FormEventHandler<HTMLFormElement>;
}
