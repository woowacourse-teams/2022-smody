import { Outlet } from 'react-router-dom';
import styled from 'styled-components';

import { FlexBox, Header, Navbar } from 'components';

export const Layout = () => {
  return (
    <>
      <Header />
      <Wrapper>
        <Outlet />
      </Wrapper>
      <Navbar />
    </>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
})`
  margin: 6rem 4rem 7rem;
`;
