import { Layout } from 'Layout';
import { useEffect } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

import useAuth from 'hooks/auth/useAuth';

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
} from 'pages';
import { Test } from 'pages/Test';

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

          {/* 프로필 이미지 업로드 테스트용 라우터 */}
          <Route path="/test" element={<Test />} />

          <Route element={<PrivateOutlet isLogin={isLogin} isLoading={isLoading} />}>
            <Route path={CLIENT_PATH.PROFILE} element={<ProfilePage />} />
            <Route path={CLIENT_PATH.PROFILE_EDIT} element={<ProfileEditPage />} />
            <Route path={CLIENT_PATH.CERT} element={<CertPage />} />
          </Route>

          <Route path={CLIENT_PATH.HOME} element={<LandingPage />} />
          <Route path={CLIENT_PATH.FEED} element={<Feed />} />
          <Route path={CLIENT_PATH.SEARCH} element={<SearchPage />} />
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
