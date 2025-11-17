import { CanActivateFn, Router } from '@angular/router';

export const adminGuard: CanActivateFn = (route, state) => {
  const user = JSON.parse(localStorage.getItem('user') || '{}');

  if (user && user.role === 'ADMIN') {
    return true; // Allow admin access
  }

  // Redirect unauthorized users
  alert('Access denied! Admins only.');
  const router = new Router();
  router.navigate(['/home']);
  return false;
};
