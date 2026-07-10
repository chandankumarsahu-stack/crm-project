import { Component, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive, Router, NavigationEnd } from '@angular/router';
import { AuthService } from './api.service';
import { ThemeService } from './theme.service';
//import { SwUpdate, VersionReadyEvent } from '@angular/service-worker';
//import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark px-3" *ngIf="auth.isLoggedIn()">
      <a class="navbar-brand" routerLink="/dashboard">ZING CRM</a>

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

      <button class="btn btn-outline-light btn-sm" (click)="logout()">
        Logout
      </button>
    </nav>

    <div class="container-fluid mt-3">
      <router-outlet></router-outlet>
    </div>
  `
})
export class AppComponent {

  private lastRoute = '/dashboard';

  constructor(
  public auth: AuthService,
  private router: Router,
  public theme: ThemeService,
  //private swUpdate: SwUpdate
) {
/*if (this.swUpdate.isEnabled) {

  this.swUpdate.versionUpdates.subscribe(() => {

    if (confirm('🚀 A new version of ZING CRM is available.\n\nReload now?')) {

      location.reload();

    }

  });

}*/
    this.router.events.subscribe(event => {
  if (event instanceof NavigationEnd && this.router.url !== '/offline') {
    this.lastRoute = this.router.url;
  }
});
  }

  @HostListener('window:offline')
  onOffline() {
    this.router.navigate(['/offline']);
  }

  @HostListener('window:online')
  onOnline() {
    if (this.router.url === '/offline') {
      this.router.navigateByUrl(this.lastRoute);
    }
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}