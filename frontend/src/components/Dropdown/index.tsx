import { DropdownProps } from './type';
import { MouseEventHandler, useState } from 'react';
import { Link } from 'react-router-dom';
import styled, { css } from 'styled-components';

const listData = [
  {
    text: '운동 챌린지를 성공하셨습니다',
    linkTo: '/cert',
  },
  {
    text: '빅터님이 댓글을 달았습니다',
    linkTo: '/feed',
  },
  {
    text: '더즈님이 댓글을 달았습니다',
    linkTo: '/profile',
  },
  {
    text: '미라클 모닝 인증 마감까지 2시간 남았습니다',
    linkTo: '/search',
  },
];

export const Dropdown = ({ children }: DropdownProps) => {
  const [isDropdownToggled, setDropdownToggled] = useState(false);
  const showDropdownMenu = () => {
    setDropdownToggled(true);
  };

  const hideDropdownMenu: MouseEventHandler<HTMLDivElement> = (event) => {
    if (event.currentTarget === event.target) {
      setDropdownToggled(false);
    }
  };

  const onSelectMenu: MouseEventHandler<HTMLUListElement> = (event) => {
    if (event.target instanceof HTMLAnchorElement) {
      setDropdownToggled(false);
    }
  };

  return (
    <Wrapper isDropdownToggled={isDropdownToggled} onClick={hideDropdownMenu}>
      <div onClick={showDropdownMenu}>{children}</div>
      {isDropdownToggled && (
        <DropdownMenu>
          <List onClick={onSelectMenu}>
            {listData.map(({ text, linkTo }) => (
              <Item key={text}>
                <Link to={linkTo}>{text}</Link>
              </Item>
            ))}
          </List>
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
    right: -0.4rem;
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

const Item = styled.li`
  ${({ theme }) => css`
    height: 2rem;
    display: flex;
    align-items: center;
    padding: 0 0.8rem;
    width: 100%;
    &:hover {
      background-color: ${theme.primary};
      color: ${theme.onPrimary};
    }
  `}
`;
