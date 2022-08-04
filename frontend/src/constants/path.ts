export const BASE_URL = process.env.BASE_URL ?? 'http://localhost:3000';

export const API_PATH = {
  CYCLE: '/cycles',
  CYCLES_ID: '/cycles/:cycleId',
};

export const CLIENT_PATH = {
  HOME: '/home',
  LOGIN: '/login',
  SIGN_UP: '/sign_up',
  CERT: '/cert',
  SEARCH: '/search',
  FEED: '/feed',
  PROFILE: '/profile',
  PROFILE_EDIT: '/profile/edit',
  CYCLE_DETAIL: '/cycle/detail',
  CYCLE_DETAIL_ID: '/cycle/detail/:cycleId',
  CHALLENGE_DETAIL: '/challenge/detail',
  CHALLENGE_DETAIL_ID: '/challenge/detail/:challengeId',
  VOC: '/voc',
  NOT_FOUND: '/not_found',
  WILD_CARD: '*',
};
