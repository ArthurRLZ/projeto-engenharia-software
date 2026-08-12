import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-admin',
  imports: [CommonModule, RouterLink],
  templateUrl: './admin.html',
  styleUrl: './admin.css',
})
export class Admin implements OnInit {
  private http = inject(HttpClient);
  private authService = inject(AuthService);

  userEmail: string | null = null;

  ngOnInit(): void {
    this.userEmail = this.authService.getEmail();
  }

  logout(): void {
    this.authService.logout();
  }
}
