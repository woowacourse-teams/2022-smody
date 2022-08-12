import { SearchBarProps } from './type';
import Search from 'assets/search.svg';
import styled, { css } from 'styled-components';

export const SearchBar = ({ search, handleSubmitSearch }: SearchBarProps) => {
  return (
    <InputWrapper onSubmit={handleSubmitSearch}>
      <InputElement type="text" placeholder="챌린지 검색" {...search} />
      <button type="submit">
        <Search />
      </button>
    </InputWrapper>
  );
};

const InputWrapper = styled.form`
  ${({ theme }) => css`
    display: flex;
    border: 1px solid ${theme.input};
    background-color: ${theme.input};

    border-radius: 20px;
    padding: 0.5rem;
    margin-bottom: 1rem;

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
