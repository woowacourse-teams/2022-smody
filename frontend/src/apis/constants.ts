export const PAGE_SIZE = {
  ALL_CHALLENGES: 10,
  SUCCESS_CHALLENGES: 10,
  FEEDS: 10,
};

export const SORT = {
  PROGRESS_TIME: 'progressTime',
  LATEST: 'latest',
};

export const ORDER = {
  DESC: 'desc',
  ASC: 'asc',
};

export const queryKeys = {
  getAllChallenges: 'getAllChallenges',
  getMySuccessChallenges: 'getMySuccessChallenges',
  getChallengeById: 'getChallengeById',
  getMyCyclesInProgress: 'getMyCyclesInProgress',
  getMyCyclesStat: 'getMyCyclesStat',
  getCycleById: 'getCycleById',
  getLinkGoogle: 'getLinkGoogle',
  getTokenGoogle: 'getTokenGoogle',
  getMyInfo: 'getMyInfo',
  getChallengersById: 'getChallengersById',
  getAllFeeds: 'getAllFeeds',
  getVapidPublicKey: 'getVapidPublicKey',
  getFeedById: 'getFeedById',
  getCommentsById: 'getCommentsById',
};

export const mutationKeys = {
  patchMyInfo: 'patchMyInfo',
  deleteMyInfo: 'deleteMyInfo',
  postProfileImage: 'postProfileImage',
  postSubscribe: 'postSubscribe',
  postUnsubscribe: 'postUnsubscribe',
};
