import { Layout } from 'Layout';
import { BrowserRouter, Routes, Route, useLocation } from 'react-router-dom';
import { LocationState } from 'types/internal';

import {
  FeedPage,
  FeedDetailPage,
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
  ChallengeRecordsPage,
  CycleDetailSharePage,
  RankingPage,
  ChallengePage,
} from 'pages';

import { PrivateOutlet, LandingNavigation } from 'components';

import { CLIENT_PATH } from 'constants/path';

const CertFlowPage = () => {
  const { state } = useLocation();
  const locationState = state as LocationState;
  if (state === null || locationState.isInCertFormPage === false) {
    return <CertPage />;
  }

  return <CertFormPage />;
};

const Router = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route index element={<LandingNavigation />} />

          <Route element={<PrivateOutlet />}>
            <Route path={CLIENT_PATH.PROFILE} element={<ProfilePage />} />
            <Route path={CLIENT_PATH.PROFILE_EDIT} element={<ProfileEditPage />} />
            <Route path={CLIENT_PATH.CERT} element={<CertFlowPage />} />
            <Route
              path={CLIENT_PATH.CHALLENGE_CREATE}
              element={<ChallengeCreatePage />}
            />
            <Route
              path={CLIENT_PATH.PROFILE_CHALLENGE_DETAIL_ID}
              element={<ChallengeRecordsPage />}
            />
            <Route
              path={CLIENT_PATH.CYCLE_DETAIL_SHARE_ID}
              element={<CycleDetailSharePage />}
            />
          </Route>

          <Route path={CLIENT_PATH.HOME} element={<LandingPage />} />
          <Route path={CLIENT_PATH.FEED} element={<FeedPage />} />
          <Route path={CLIENT_PATH.FEED_DETAIL_ID} element={<FeedDetailPage />} />
          <Route path={CLIENT_PATH.CYCLE_DETAIL_ID} element={<CycleDetailPage />} />
          <Route
            path={CLIENT_PATH.CHALLENGE_DETAIL_ID}
            element={<ChallengeDetailPage />}
          />

          <Route path={CLIENT_PATH.RANK} element={<RankingPage />} />

          <Route path={CLIENT_PATH.CHALLENGE_EVENT} element={<ChallengePage />} />
          <Route path={CLIENT_PATH.CHALLENGE_SEARCH} element={<ChallengePage />} />

          <Route path={CLIENT_PATH.VOC} element={<VocPage />} />

          <Route path={CLIENT_PATH.NOT_FOUND} element={<NotFoundPage />} />
          <Route path={CLIENT_PATH.WILD_CARD} element={<NotFoundPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default Router;
