import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-home',
  imports: [CommonModule, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  private authService = inject(AuthService);

  userEmail: string | null = null;
  userRole: string | null = null;
  isAdmin = false;

  ngOnInit(): void {
    this.userEmail = this.authService.getEmail();
    this.userRole = this.authService.getRole();
    this.isAdmin = this.userRole === 'ADMIN';
  }

  logout(): void {
    this.authService.logout();
  }
}
