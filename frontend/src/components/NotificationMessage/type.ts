export interface NotificationMessageProps {
  updateNotificationCount: (updatedNotificationCount: number) => void;
}

export type UseNotificationMessageProps = NotificationMessageProps;

export interface NotificationHandlerProps {
  pushNotificationId: number;
  pathId?: number;
  pushCase?: string;
}
