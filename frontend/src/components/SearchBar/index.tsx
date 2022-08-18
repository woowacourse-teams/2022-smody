import { SearchBarProps } from './type';
import Search from 'assets/search.svg';
import styled, { css } from 'styled-components';

const horizontalPadding = { pc: '10rem', tablet: '7rem', mobile: '1.25rem' };

export const SearchBar = ({ search, handleSubmitSearch }: SearchBarProps) => {
  return (
    <Background>
      <InputWrapper onSubmit={handleSubmitSearch}>
        <InputElement type="text" placeholder="챌린지 검색" {...search} />
        <button type="submit">
          <Search />
        </button>
      </InputWrapper>
    </Background>
  );
};

const Background = styled.div`
  ${({ theme }) => css`
    position: fixed;
    top: 60px;
    background-color: ${theme.background};
    padding: 5px 0;

    @media all and (min-width: 1024px) {
      left: ${horizontalPadding.pc};
      right: ${horizontalPadding.pc};
    }

    /* 테블릿 가로, 테블릿 세로 (해상도 768px ~ 1023px)*/
    @media all and (min-width: 768px) and (max-width: 1023px) {
      left: ${horizontalPadding.tablet};
      right: ${horizontalPadding.tablet};
    }

    /* 모바일 가로, 모바일 세로 (해상도 480px ~ 767px)*/
    @media all and (max-width: 767px) {
      left: ${horizontalPadding.mobile};
      right: ${horizontalPadding.mobile};
    }
  `}
`;

const InputWrapper = styled.form`
  ${({ theme }) => css`
    display: flex;
    border: 1px solid ${theme.input};
    background-color: ${theme.input};

    border-radius: 20px;
    padding: 0.5rem;

    & svg path {
      fill: ${theme.primary};
      stroke: none;
    }
  `}
`;

const InputElement = styled.input`
  ${({ theme }) => css`
    background-color: transparent;
    border: none;
    width: 100%;
    margin-left: 0.5rem;
    outline: none;
    font-size: 1rem;
    color: ${theme.onInput};
  `}
`;
