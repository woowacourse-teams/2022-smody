import { CheckSuccessProps } from './type';
import { FaCrown } from 'react-icons/fa';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

export const CheckSuccessCycle = ({ isSuccess }: CheckSuccessProps) => {
  const themeContext = useThemeContext();

  if (!isSuccess) {
    return null;
  }

  return (
    <SuccessIconWrapper>
      <FaCrown color={themeContext.primary} size={25} />
    </SuccessIconWrapper>
  );
};

const SuccessIconWrapper = styled.div`
  margin-left: 5px;
`;
