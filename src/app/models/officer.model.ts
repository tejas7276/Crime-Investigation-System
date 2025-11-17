import { User } from './user.model';

export interface Officer extends User {
  badgeNumber: string;
  name: string;
  department: string;
}
