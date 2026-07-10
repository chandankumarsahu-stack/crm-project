import { Routes } from '@angular/router';
import { authGuard } from './auth.guard';
import {
  LoginComponent, DashboardComponent, LeadTypeComponent,
  CustomerLeadComponent, RemindersComponent
} from './pages';
import { OfflineComponent } from './offline/offline.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'lead-types', component: LeadTypeComponent, canActivate: [authGuard] },
  { path: 'leads', component: CustomerLeadComponent, canActivate: [authGuard] },
  { path: 'reminders', component: RemindersComponent, canActivate: [authGuard] },
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: '**', redirectTo: 'dashboard' },
  {
    path:'offline',
    component:OfflineComponent
},
];
