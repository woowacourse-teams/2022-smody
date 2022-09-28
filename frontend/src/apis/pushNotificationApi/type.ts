export interface GetVapidPublicKeyResponse {
  publicKey: string;
}

export interface PostUnsubscribePayload {
  endpoint: string;
}

export interface DeleteNotificationParams {
  pushNotificationId: number;
}

interface CustomNotification {
  pushNotificationId: number;
  message: string;
  pushTime: string;
  pathId: number;
  pushCase: string;
}

export type GetNotificationsResponse = CustomNotification[];
