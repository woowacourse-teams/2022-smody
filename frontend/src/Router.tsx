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

import { CLIENT_PATH } from 'constants/path';

const Router = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path={CLIENT_PATH.HOME} element={<Home />} />
          <Route path={CLIENT_PATH.LOGIN} element={<LoginPage />} />
          <Route path={CLIENT_PATH.SIGN_UP} element={<SignUpPage />} />
          <Route path={CLIENT_PATH.SEARCH} element={<SearchPage />} />
          <Route
            path={CLIENT_PATH.CHALLENGE_DETAIL_ID}
            element={<ChallengeDetailPage />}
          />
          <Route path={CLIENT_PATH.CERT} element={<CertPage />} />
          <Route path={CLIENT_PATH.NOT_FOUND} element={<NotFoundPage />} />
          <Route path={'*'} element={<NotFoundPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default Router;
