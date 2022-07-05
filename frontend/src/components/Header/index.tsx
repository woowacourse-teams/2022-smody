import { useContext } from 'react';
import styled, { ThemeContext, css } from 'styled-components';

import { Logo, FlexBox, LinkText } from 'components';

export const Header = () => {
  const themeContext = useContext(ThemeContext);
  return (
    <Wrapper>
      <Logo width="100" color={themeContext.onPrimary} />
      <LinkText to="/login" size={20} color={themeContext.onPrimary}>
        로그인
      </LinkText>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  justifyContent: 'space-between',
  alignItems: 'center',
})`
  ${({ theme }) => css`
    background-color: ${theme.primary};
    padding: 1.2rem 4rem;
  `}
`;
