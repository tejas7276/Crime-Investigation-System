// crime.model.ts
import { User } from './user.model';
import { Notification } from './notification.model';

export interface Crime {
  id: number;
  description?: string;
  type: string;
  location: string;
  status: string;
  date: string;
  time: string;
  userId?: number;
  officerId?: number | null;
  officername?: string;
  assignedOfficer?: {
    id: number | null;
    name: string;
  };
  assignedOfficerId?: number;
  notifications?: Notification[]; // Add notifications array
}
