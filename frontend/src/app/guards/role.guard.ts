import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // pega as roles liberadas no routes
  const expectedRoles = route.data['roles'] as Array<string>;

  if (!expectedRoles || expectedRoles.length === 0) {
    return true; 
  }

  if (!authService.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  const userRole = authService.getRole();

  // ve se o usuario tem a role certa
  if (userRole && expectedRoles.includes(userRole)) {
    return true;
  }

  // sem permissao, volta pra home
  router.navigate(['/home']);
  return false;
};
