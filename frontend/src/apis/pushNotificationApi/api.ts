import { apiClient, authApiClient } from 'apis/apiClient';

export const getVapidPublicKey = async () => {
  return apiClient.axios.get('/web-push/public-key');
};

export const postSubscribe = async (subscription: PushSubscription) => {
  return authApiClient.axios.post('/web-push/subscribe', subscription);
};

export const postUnsubscribe = async (endpoint: string) => {
  return authApiClient.axios.post('/web-push/unsubscribe', endpoint);
};
