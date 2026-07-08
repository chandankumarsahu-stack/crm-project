import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AuthService } from './api.service';
import { ThemeService } from './theme.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark px-3" *ngIf="auth.isLoggedIn()">
      <a class="navbar-brand" routerLink="/dashboard">📋 Lead CRM</a>
      <div class="navbar-nav me-auto">
        <a class="nav-link" routerLink="/dashboard" routerLinkActive="active fw-bold">Dashboard</a>
        <a class="nav-link" routerLink="/leads" routerLinkActive="active fw-bold">Customer Leads</a>
        <a class="nav-link" routerLink="/lead-types" routerLinkActive="active fw-bold">Lead Types</a>
        <a class="nav-link" routerLink="/reminders" routerLinkActive="active fw-bold">Reminders</a>
      </div>
      <span class="text-light me-3">Hi, {{ auth.currentUser() }}</span>
      <button class="btn btn-modern me-2" (click)="theme.toggleTheme()">
  🌙 / ☀
</button>
      <button class="btn btn-outline-light btn-sm" (click)="logout()">Logout</button>
    </nav>
    <div class="container-fluid mt-3">
      <router-outlet></router-outlet>
    </div>
  `
})
export class AppComponent {
  constructor(public auth: AuthService, private router: Router, public theme:ThemeService) {}

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
