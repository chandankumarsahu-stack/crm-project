import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import {
  LeadType, CustomerLead, FollowUp, Note, DashboardStats, LeadSearchCriteria
} from './models';

const BASE = 'http://localhost:8080/api';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private loggedInSubject = new BehaviorSubject<boolean>(!!localStorage.getItem('crm_user'));
  loggedIn$ = this.loggedInSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${BASE}/auth/login`, { username, password }).pipe(
      tap(res => {
        if (res.success) {
          localStorage.setItem('crm_user', res.username);
          this.loggedInSubject.next(true);
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem('crm_user');
    this.loggedInSubject.next(false);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('crm_user');
  }

  currentUser(): string | null {
    return localStorage.getItem('crm_user');
  }
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private http: HttpClient) {}

  // ---- Lead Types ----
  getLeadTypes(): Observable<LeadType[]> { return this.http.get<LeadType[]>(`${BASE}/lead-types`); }
  createLeadType(lt: LeadType): Observable<LeadType> { return this.http.post<LeadType>(`${BASE}/lead-types`, lt); }
  updateLeadType(id: number, lt: LeadType): Observable<LeadType> { return this.http.put<LeadType>(`${BASE}/lead-types/${id}`, lt); }
  deleteLeadType(id: number): Observable<void> { return this.http.delete<void>(`${BASE}/lead-types/${id}`); }

  // ---- Customer Leads ----
  getLeads(): Observable<CustomerLead[]> { return this.http.get<CustomerLead[]>(`${BASE}/leads`); }
  getLead(id: number): Observable<CustomerLead> { return this.http.get<CustomerLead>(`${BASE}/leads/${id}`); }
  createLead(payload: any): Observable<CustomerLead> { return this.http.post<CustomerLead>(`${BASE}/leads`, payload); }
  updateLead(id: number, payload: any): Observable<CustomerLead> { return this.http.put<CustomerLead>(`${BASE}/leads/${id}`, payload); }
  deleteLead(id: number): Observable<void> { return this.http.delete<void>(`${BASE}/leads/${id}`); }
  searchLeads(criteria: LeadSearchCriteria): Observable<CustomerLead[]> { return this.http.post<CustomerLead[]>(`${BASE}/leads/search`, criteria); }

  // ---- Dashboard & Reminders ----
  getDashboardStats(): Observable<DashboardStats> { return this.http.get<DashboardStats>(`${BASE}/leads/dashboard`); }
  getReminders(): Observable<CustomerLead[]> { return this.http.get<CustomerLead[]>(`${BASE}/leads/reminders`); }

  // ---- Follow-ups ----
  getFollowups(leadId: number): Observable<FollowUp[]> { return this.http.get<FollowUp[]>(`${BASE}/leads/${leadId}/followups`); }
  addFollowup(leadId: number, payload: any): Observable<FollowUp> { return this.http.post<FollowUp>(`${BASE}/leads/${leadId}/followups`, payload); }

  // ---- Notes ----
  getNotes(leadId: number): Observable<Note[]> { return this.http.get<Note[]>(`${BASE}/leads/${leadId}/notes`); }
  addNote(leadId: number, payload: any): Observable<Note> { return this.http.post<Note>(`${BASE}/leads/${leadId}/notes`, payload); }
  deleteNote(noteId: number): Observable<void> { return this.http.delete<void>(`${BASE}/leads/notes/${noteId}`); }
}
