import { Layout } from 'Layout';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

import useAuth from 'hooks/useAuth';

import {
  Feed,
  SearchPage,
  ChallengeDetailPage,
  NotFoundPage,
  CertPage,
  LandingPage,
  ProfilePage,
  ProfileEditPage,
  VocPage,
  CycleDetailPage,
  CertFormPage,
} from 'pages';

import { PrivateOutlet, LandingNavigation } from 'components';

import { CLIENT_PATH } from 'constants/path';

const Router = () => {
  const { isLogin, isLoading } = useAuth();

  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route
            index
            element={<LandingNavigation isLogin={isLogin} isLoading={isLoading} />}
          />

          <Route element={<PrivateOutlet isLogin={isLogin} isLoading={isLoading} />}>
            <Route path={CLIENT_PATH.PROFILE} element={<ProfilePage />} />
            <Route path={CLIENT_PATH.PROFILE_EDIT} element={<ProfileEditPage />} />
            <Route path={CLIENT_PATH.CERT} element={<CertPage />} />
          </Route>

          <Route path={CLIENT_PATH.HOME} element={<LandingPage />} />
          <Route path={CLIENT_PATH.FEED} element={<Feed />} />
          <Route path={CLIENT_PATH.SEARCH} element={<SearchPage />} />
          <Route path={CLIENT_PATH.CYCLE_DETAIL_ID} element={<CycleDetailPage />} />
          <Route
            path={CLIENT_PATH.CHALLENGE_DETAIL_ID}
            element={<ChallengeDetailPage />}
          />
          <Route path={CLIENT_PATH.CERT_FORM} element={<CertFormPage />} />
          <Route path={CLIENT_PATH.VOC} element={<VocPage />} />
          <Route path={CLIENT_PATH.NOT_FOUND} element={<NotFoundPage />} />
          <Route path={CLIENT_PATH.WILD_CARD} element={<NotFoundPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default Router;
