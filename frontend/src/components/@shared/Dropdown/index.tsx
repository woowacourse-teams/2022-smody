import { DropdownProps, WrapperProps } from './type';
import { useDropdown } from './useDropdown';
import { PropsWithChildren } from 'react';
import styled, { css } from 'styled-components';

import { Z_INDEX } from 'constants/css';

export const Dropdown = ({
  disabled = false,
  button,
  nonLinkableElement,
  children,
  updateNotificationCount,
  updateIsSubscribed,
}: PropsWithChildren<DropdownProps>) => {
  const { isDropdownToggled, showDropdownMenu, hideDropdownMenu, onSelectMenu } =
    useDropdown({ updateNotificationCount, updateIsSubscribed });

  return (
    <Wrapper isDropdownToggled={isDropdownToggled} onClick={hideDropdownMenu}>
      <DropdownButton disabled={disabled} onClick={showDropdownMenu}>
        {button}
      </DropdownButton>
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
    z-index: ${Z_INDEX.CSS_MODAL_BG};
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    display: block;
    cursor: default;
    content: ' ';
    background: transparent;
  }
`;

const Wrapper = styled.div<WrapperProps>`
  ${({ isDropdownToggled }) => css`
    cursor: pointer;
    position: relative;

    ${isDropdownToggled && EntireBackground}
  `}
`;

const DropdownButton = styled.button`
  ${({ theme }) => css`
    padding: 0;

    &:disabled {
      & svg path {
        fill: ${theme.disabled};
      }
    }
  `}
`;

const DropdownMenu = styled.div`
  ${({ theme }) => css`
    z-index: ${Z_INDEX.CSS_MODAL};
    height: fit-content;
    overflow-y: scroll;
    overflow-x: hidden;
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

    width: 80vw;
    max-width: 400px;

    // 드롭다운 메뉴 우측 상단 삼각형 팁 디자인
    // overflow: auto 때문에 삼각형 팁 디자인이 현재 적용되지 않음
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

    // 스크롤바
    /* 스크롤바 설정*/
    &::-webkit-scrollbar {
      width: 4px;
    }

    /* 스크롤바 막대 설정*/
    &::-webkit-scrollbar-thumb {
      height: 17%;
      background-color: ${theme.primary};
      border-radius: 100px;
    }

    /* 스크롤바 뒷 배경 설정*/
    &::-webkit-scrollbar-track {
      background-color: ${theme.surface};
      border-radius: 100px;
    }
  `}
`;

const List = styled.ul`
  margin: 0.4rem 0;
`;
