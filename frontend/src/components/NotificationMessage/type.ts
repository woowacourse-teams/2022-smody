export type NotificationMessageProps = {
  updateNotificationCount: (updatedNotificationCount: number) => void;
};

export type UseNotificationMessageProps = NotificationMessageProps;

export type NotificationHandlerProps = {
  pushNotificationId: number;
  pathId?: number;
  pushCase?: string;
};
