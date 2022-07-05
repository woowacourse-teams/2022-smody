import { Layout } from 'Layout';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

import { SignUpPage, LoginPage, NotFoundPage } from 'pages';

import { PATH } from 'constants/path';

const Router = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path={PATH.LOGIN} element={<LoginPage />} />
          <Route path={PATH.SIGN_UP} element={<SignUpPage />} />
          <Route path={PATH.NOT_FOUND} element={<NotFoundPage />} />
          <Route path={'*'} element={<NotFoundPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default Router;
