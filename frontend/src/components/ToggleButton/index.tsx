import { ToggleButtonProps } from './type';
import styled, { css } from 'styled-components';

export const ToggleButton = ({ checked, handleChange, disabled }: ToggleButtonProps) => {
  return (
    <CheckBoxWrapper>
      <CheckBox
        id="checkbox"
        type="checkbox"
        checked={checked}
        onChange={handleChange}
        disabled={disabled}
      />
      <CheckBoxLabel htmlFor="checkbox" disabled={disabled} />
    </CheckBoxWrapper>
  );
};

const CheckBoxWrapper = styled.div`
  position: relative;
`;

const CheckBoxLabel = styled.label<{ disabled: boolean | undefined }>`
  ${({ disabled }) => css`
    position: absolute;
    top: 0;
    left: 0;
    width: 42px;
    height: 26px;
    border-radius: 15px;
    background: ${disabled ? '#e6e6e6' : '#c8bcff'};
    cursor: pointer;
    &::after {
      content: '';
      display: block;
      border-radius: 50%;
      width: 18px;
      height: 18px;
      margin: 3px;
      background: ${disabled ? '#9a9a9a' : '#ffffff'};
      box-shadow: 1px 3px 3px 1px rgba(0, 0, 0, 0.2);
      transition: 0.2s;
    }
  `}
`;

const CheckBox = styled.input`
  opacity: 0;
  z-index: 1;
  border-radius: 15px;
  width: 42px;
  height: 26px;
  &:checked + ${CheckBoxLabel} {
    background: #7b61ff;
    &::after {
      content: '';
      display: block;
      border-radius: 50%;
      width: 18px;
      height: 18px;
      margin-left: 21px;
      transition: 0.2s;
    }
  }
`;
