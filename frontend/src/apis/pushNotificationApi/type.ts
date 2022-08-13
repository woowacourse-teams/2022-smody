export interface GetVapidPublicKeyResponse {
  publicKey: string;
}

export interface PostUnsubscribeProps {
  endpoint: string;
}

export interface DeleteNotificationProps {
  pushNotificationId: number;
}

interface CustomNotification {
  pushNotificationId: number;
  message: string;
  pushTime: string;
}

export type GetNotificationsResponse = CustomNotification[];
