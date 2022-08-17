import { SelectOptions } from './type';
import { useState } from 'react';

export const useCycleRecords = () => {
  const [selectOption, setSelectOption] = useState<SelectOptions>('all');

  const handleSelectButton = (select: SelectOptions) => {
    if (select === selectOption) {
      return;
    }
    setSelectOption(select);
  };

  return {
    selectOption,
    handleSelectButton,
  };
};
