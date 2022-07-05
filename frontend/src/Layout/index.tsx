import { Outlet } from 'react-router-dom';

import { Header, Navbar } from 'components';

export const Layout = () => {
  return (
    <>
      <Header />
      <Outlet />
      <Navbar />
    </>
  );
};
