import CorrectIcon from 'assets/correct_icon.svg';
import IncorrectIcon from 'assets/incorrect_icon.svg';
import styled, { css } from 'styled-components';

import { ValidationMessageProps } from 'components/ValidationMessage/type';

export const ValidationMessage = ({
  isValidated,
  value,
  message,
}: ValidationMessageProps) => {
  if (typeof isValidated === 'undefined') {
    return null;
  }

  if (!value) {
    return null;
  }

  return (
    <ValidationWrapper>
      {isValidated ? <CorrectIcon /> : <IncorrectIcon />}
      <Message isValidated={isValidated}>{message}</Message>
    </ValidationWrapper>
  );
};

const ValidationWrapper = styled.div`
  display: flex;
  align-items: center;
  height: 2rem;
  padding-left: 2px;
`;

const Message = styled.span<Required<Pick<ValidationMessageProps, 'isValidated'>>>`
  ${({ theme, isValidated }) => css`
    color: ${isValidated ? theme.success : theme.error};
    margin-left: 0.5rem;
  `}
`;
