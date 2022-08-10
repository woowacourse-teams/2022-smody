import { DarkModeButtonProps } from './type';
import styled from 'styled-components';

export const DarkModeButton = ({ checked, handleChange }: DarkModeButtonProps) => {
  return (
    <CheckBoxWrapper>
      <CheckBox
        id="darkModeCheckbox"
        type="checkbox"
        checked={checked}
        onChange={handleChange}
      />
      <CheckBoxLabel htmlFor="darkModeCheckbox" />
    </CheckBoxWrapper>
  );
};

const CheckBoxWrapper = styled.div`
  position: relative;
`;

const CheckBoxLabel = styled.label`
  content: '';
  display: inline-block;
  cursor: pointer;
  height: 23px;
  width: 23px;
  border-radius: 50%;
  transition: all 0.3s ease-in-out;
`;

const CheckBox = styled.input`
  visibility: hidden;

  &:not(:checked) + ${CheckBoxLabel} {
    background-image: url("data:image/svg+xml,%3Csvg style='color: rgb(123, 97, 235);' xmlns='http://www.w3.org/2000/svg' width='23' height='23' fill='currentColor' class='bi bi-sun' viewBox='0 0 16 16'%3E%3Cpath d='M8 11a3 3 0 1 1 0-6 3 3 0 0 1 0 6zm0 1a4 4 0 1 0 0-8 4 4 0 0 0 0 8zM8 0a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-1 0v-2A.5.5 0 0 1 8 0zm0 13a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-1 0v-2A.5.5 0 0 1 8 13zm8-5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1 0-1h2a.5.5 0 0 1 .5.5zM3 8a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1 0-1h2A.5.5 0 0 1 3 8zm10.657-5.657a.5.5 0 0 1 0 .707l-1.414 1.415a.5.5 0 1 1-.707-.708l1.414-1.414a.5.5 0 0 1 .707 0zm-9.193 9.193a.5.5 0 0 1 0 .707L3.05 13.657a.5.5 0 0 1-.707-.707l1.414-1.414a.5.5 0 0 1 .707 0zm9.193 2.121a.5.5 0 0 1-.707 0l-1.414-1.414a.5.5 0 0 1 .707-.707l1.414 1.414a.5.5 0 0 1 0 .707zM4.464 4.465a.5.5 0 0 1-.707 0L2.343 3.05a.5.5 0 1 1 .707-.707l1.414 1.414a.5.5 0 0 1 0 .708z' fill='%237b61ff'%3E%3C/path%3E%3C/svg%3E");
  }

  &:checked + ${CheckBoxLabel} {
    background: transparent;
    box-shadow: inset -5.1px -4.6px 1px 1px #ded9f6;
  }
`;
