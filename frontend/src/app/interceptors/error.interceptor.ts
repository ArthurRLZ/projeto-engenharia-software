import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const authService = inject(AuthService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      // Se der 401 (Nao autorizado) ou 403 (Proibido), provavel que o token expirou
      if (error.status === 401 || error.status === 403) {
        alert('Sua sessão expirou ou você não tem permissão. Faça login novamente.');
        authService.logout();
      } else if (error.status >= 500) {
        alert('Ocorreu um erro no servidor. Tente novamente mais tarde.');
      } else if (error.status === 400 || error.status === 404 || error.status === 409) {
        // Para erros 400 (Bad Request) ou 409 (Conflict), normalmente o backend envia uma mensagem
        const errorMessage = error.error?.message || error.error || 'Ocorreu um erro na requisição.';
        // Não usar alert aqui para não travar a tela em toda validação de form,
        // mas é bom repassar o erro para o componente lidar (ex: exibir no HTML).
        console.error('Erro tratado:', errorMessage);
      }
      
      return throwError(() => error);
    })
  );
};
