import { Layout } from 'Layout';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

import {
  Home,
  SignUpPage,
  LoginPage,
  SearchPage,
  ChallengeDetailPage,
  NotFoundPage,
  CertPage,
} from 'pages';

import { PATH } from 'constants/path';

const Router = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path={PATH.HOME} element={<Home />} />
          <Route path={PATH.LOGIN} element={<LoginPage />} />
          <Route path={PATH.SIGN_UP} element={<SignUpPage />} />
          <Route path={PATH.SEARCH} element={<SearchPage />} />
          <Route path={PATH.CHALLENGE_DETAIL_ID} element={<ChallengeDetailPage />} />
          <Route path={PATH.CERT} element={<CertPage />} />
          <Route path={PATH.NOT_FOUND} element={<NotFoundPage />} />
          <Route path={'*'} element={<NotFoundPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default Router;
