import { DeleteNotificationParams, PostUnsubscribePayload } from './type';
import { apiClient, authApiClient } from 'apis/apiClient';

export const getVapidPublicKey = async () => {
  return apiClient.axios.get('/web-push/public-key');
};

export const postSubscribe = async (subscription: PushSubscription) => {
  return authApiClient.axios.post('/web-push/subscribe', subscription);
};

export const postUnsubscribe = async ({ endpoint }: PostUnsubscribePayload) => {
  return authApiClient.axios.post('/web-push/unsubscribe', { endpoint });
};

export const getNotifications = async () => {
  return authApiClient.axios.get('/push-notifications');
};

export const deleteNotification = async ({
  pushNotificationId,
}: DeleteNotificationParams) => {
  return authApiClient.axios.delete(`/push-notifications/${pushNotificationId}`);
};

export const deleteAllNotification = async () => {
  return authApiClient.axios.delete('/push-notifications/me');
};
