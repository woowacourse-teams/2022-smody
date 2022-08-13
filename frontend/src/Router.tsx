import { Layout } from 'Layout';
import { useGetMyInfo } from 'apis';
import { BrowserRouter, Routes, Route, useLocation } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import {
  FeedPage,
  FeedDetailPage,
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
  emojiIndex?: number;
  colorIndex?: number;
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
  const setIsLogin = useSetRecoilState(isLoginState);

  useGetMyInfo({
    onSuccess: () => {
      setIsLogin(true);
    },
  });

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
          </Route>

          <Route path={CLIENT_PATH.HOME} element={<LandingPage />} />
          <Route path={CLIENT_PATH.FEED} element={<FeedPage />} />
          <Route path={CLIENT_PATH.FEED_DETAIL_ID} element={<FeedDetailPage />} />
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
