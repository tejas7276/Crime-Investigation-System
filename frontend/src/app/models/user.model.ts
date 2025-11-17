export interface User {
  id: number;
  username: string;
  password: string;
  email: string;
  role: string; // e.g., CITIZEN, OFFICER, ADMIN
  firstname: string;
  lastname: string;
}
