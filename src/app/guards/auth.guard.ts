import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  const router = inject(Router);

  if (user && user.username) {
    return true;
  }

  alert('Access denied! Please log in.');
  router.navigate(['/login']);
  return false;
};
