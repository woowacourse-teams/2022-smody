export const PAGE_SIZE = {
  ALL_CHALLENGES: 10,
  ALL_MEMBERS: 20,
  SUCCESS_CHALLENGES: 10,
  CYCLES: 10,
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
  getMyChallenges: 'getMyChallenges',
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
  getNotifications: 'getNotifications',
  getMyChallengeById: 'getMyChallengeById',
  getMyCyclesByChallengeId: 'getMyCyclesByChallengeId',
  getIsValidAccessToken: 'getIsValidAccessToken',
  getMembers: 'getMembers',
  getRankingPeriods: 'getRankingPeriods',
  getMyRanking: 'getMyRanking',
  getAllRanking: 'getAllRanking',
};

export const mutationKeys = {
  patchMyInfo: 'patchMyInfo',
  deleteMyInfo: 'deleteMyInfo',
  postProfileImage: 'postProfileImage',
  postSubscribe: 'postSubscribe',
  postUnsubscribe: 'postUnsubscribe',
  deleteNotification: 'deleteNotification',
};
