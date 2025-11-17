export interface Notification {
  id: number;
  user: any; // Assuming the `user` is a User object or userId in the frontend
  message: string;
  timestamp: string | number | Date; // This will be a string (ISO 8601) representing the LocalDateTime
  status: string; // "unread" or "read"
}
