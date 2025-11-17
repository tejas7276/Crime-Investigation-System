export interface Emergency {
  id: number;
  userId: number;
  location: string | null;
  status: 'URGENT' | 'RESPONDED';
  createdAt: string; // ISO format datetime
  respondedAt: string | null;
}
