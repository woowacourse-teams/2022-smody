import { DropdownProps } from './type';
import { MouseEventHandler, useState } from 'react';
import styled, { css } from 'styled-components';

export const Dropdown = ({ button, nonLinkableElement, children }: DropdownProps) => {
  const [isDropdownToggled, setDropdownToggled] = useState(false);
  const showDropdownMenu = () => {
    setDropdownToggled(true);
  };

  const hideDropdownMenu: MouseEventHandler<HTMLDivElement> = (event) => {
    if (event.currentTarget === event.target) {
      setDropdownToggled(false);
    }
  };

  const onSelectMenu: MouseEventHandler<HTMLUListElement> = () => {
    setDropdownToggled(false);
  };

  return (
    <Wrapper isDropdownToggled={isDropdownToggled} onClick={hideDropdownMenu}>
      <div onClick={showDropdownMenu}>{button}</div>
      {isDropdownToggled && (
        <DropdownMenu>
          {nonLinkableElement}
          <List onClick={onSelectMenu}>{children}</List>
        </DropdownMenu>
      )}
    </Wrapper>
  );
};

const EntireBackground = css`
  &:before {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    z-index: 80;
    display: block;
    cursor: default;
    content: ' ';
    background: transparent;
  }
`;

const Wrapper = styled.div<{ isDropdownToggled: boolean }>`
  cursor: pointer;
  position: relative;

  ${({ isDropdownToggled }) => isDropdownToggled && EntireBackground}
`;

const DropdownMenu = styled.div`
  ${({ theme }) => css`
    height: fit-content;
    max-height: 32rem;
    white-space: nowrap;
    background-color: ${theme.surface};
    border: 1px solid ${theme.primary};
    color: ${theme.onSurface};
    font-size: 0.9rem;
    border-radius: 0.5rem;
    position: absolute;
    top: 2.3rem;
    right: -0.2rem;
    z-index: 100;

    // 드롭다운 메뉴 우측 상단 삼각형 팁 디자인
    &::after {
      top: -14px;
      right: 10px;
      content: '';
      border: 7px solid transparent;
      border-bottom-color: ${theme.surface};
      position: absolute;
    }

    &::before {
      top: -16px;
      right: 9px;
      content: '';
      border: 8px solid transparent;
      border-bottom-color: ${theme.primary};
      position: absolute;
    }
  `}
`;

const List = styled.ul`
  margin: 0.4rem 0;
`;
