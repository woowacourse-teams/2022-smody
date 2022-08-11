import { Layout } from 'Layout';
import { BrowserRouter, Routes, Route, useLocation } from 'react-router-dom';

import useAuth from 'hooks/useAuth';

import {
  FeedPage,
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
  ChallengeCreatePage,
} from 'pages';

import { PrivateOutlet, LandingNavigation } from 'components';

import { CLIENT_PATH } from 'constants/path';

interface LocationState {
  isInCertFormPage: boolean;
  cycleId?: number;
  challengeId?: number;
  challengeName?: string;
  progressCount?: number;
  successCount?: number;
  emoji?: string;
}

const CertFlowPage = () => {
  const { state } = useLocation();
  const locationState = state as LocationState;
  if (state === null || locationState.isInCertFormPage === false) {
    return <CertPage />;
  }

  return <CertFormPage />;
};

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
            <Route path={CLIENT_PATH.CERT} element={<CertFlowPage />} />
            <Route
              path={CLIENT_PATH.CHALLENGE_CREATE}
              element={<ChallengeCreatePage />}
            />
          </Route>

          <Route path={CLIENT_PATH.HOME} element={<LandingPage />} />
          <Route path={CLIENT_PATH.FEED} element={<FeedPage />} />
          <Route path={CLIENT_PATH.SEARCH} element={<SearchPage />} />
          <Route path={CLIENT_PATH.CYCLE_DETAIL_ID} element={<CycleDetailPage />} />
          <Route
            path={CLIENT_PATH.CHALLENGE_DETAIL_ID}
            element={<ChallengeDetailPage />}
          />
          <Route path={CLIENT_PATH.VOC} element={<VocPage />} />

          <Route path={CLIENT_PATH.NOT_FOUND} element={<NotFoundPage />} />
          <Route path={CLIENT_PATH.WILD_CARD} element={<NotFoundPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default Router;
