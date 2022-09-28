export type GetVapidPublicKeyResponse = {
  publicKey: string;
};

export type PostUnsubscribePayload = {
  endpoint: string;
};

export type DeleteNotificationParams = {
  pushNotificationId: number;
};

type CustomNotification = {
  pushNotificationId: number;
  message: string;
  pushTime: string;
  pathId: number;
  pushCase: string;
};

export type GetNotificationsResponse = CustomNotification[];
