import { DropdownProps } from './type';
import { MouseEventHandler, useState } from 'react';
import styled, { css } from 'styled-components';

export const useDropdown = () => {
  const [isDropdownToggled, setDropdownToggled] = useState(false);
  const showDropdownMenu = () => {
    setDropdownToggled(true);
  };

  const hideDropdownMenu: MouseEventHandler<HTMLDivElement> = (event) => {
    if (event.currentTarget === event.target) {
      setDropdownToggled(false);
    }
  };

  const onSelectMenu = () => {
    setDropdownToggled(false);
  };
  return { isDropdownToggled, showDropdownMenu, hideDropdownMenu, onSelectMenu };
};
