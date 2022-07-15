import { Layout } from 'Layout';
import { useEffect } from 'react';
import { Navigate, Outlet, BrowserRouter, Routes, Route } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import {
  Feed,
  SignUpPage,
  LoginPage,
  SearchPage,
  ChallengeDetailPage,
  NotFoundPage,
  CertPage,
  ProfilePage,
} from 'pages';

import { CLIENT_PATH } from 'constants/path';

const AuthOnly = () => {
  const accessToken = localStorage.getItem('accessToken');
  return accessToken ? <Outlet /> : <Navigate to={CLIENT_PATH.CERT} />;
};

const UnAuthOnly = () => {
  const accessToken = localStorage.getItem('accessToken');
  return !accessToken ? <Outlet /> : <Navigate to={CLIENT_PATH.PROFILE} />;
};

const Router = () => {
  const setIsLogin = useSetRecoilState(isLoginState);
  useEffect(() => {
    const accessToken = localStorage.getItem('accessToken');

    if (accessToken) {
      setIsLogin(true);
    }
  }, []);

  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route element={<AuthOnly />}>
            <Route path={CLIENT_PATH.PROFILE} element={<ProfilePage />} />
          </Route>

          <Route element={<UnAuthOnly />}>
            <Route path={CLIENT_PATH.LOGIN} element={<LoginPage />} />
            <Route path={CLIENT_PATH.SIGN_UP} element={<SignUpPage />} />
          </Route>

          <Route path={CLIENT_PATH.HOME} element={<Navigate to={CLIENT_PATH.CERT} />} />
          <Route path={CLIENT_PATH.CERT} element={<CertPage />} />
          <Route path={CLIENT_PATH.FEED} element={<Feed />} />
          <Route path={CLIENT_PATH.SEARCH} element={<SearchPage />} />
          <Route
            path={CLIENT_PATH.CHALLENGE_DETAIL_ID}
            element={<ChallengeDetailPage />}
          />
          <Route path={CLIENT_PATH.NOT_FOUND} element={<NotFoundPage />} />
          <Route path={CLIENT_PATH.WILD_CARD} element={<NotFoundPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default Router;
