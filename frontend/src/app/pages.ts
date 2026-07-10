import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService, ApiService } from './api.service';
import {
  LeadType, CustomerLead, FollowUp, Note, DashboardStats,
  STATUS_OPTIONS, PRIORITY_OPTIONS
} from './models';
import { PwaService } from './pwa.service';
import { AfterViewInit } from '@angular/core';
import { Chart } from 'chart.js/auto';

// ================= LOGIN =================
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  template: `
    <div class="login-page">

  <div class="aurora aurora-1"></div>
  <div class="aurora aurora-2"></div>
  <div class="grid-bg"></div>

  <div class="login-card glass">

    <div class="text-center mb-4">
    <img
  src="assets/images/logo.png"
  alt="ZING CRM"
  width="90"
  class="mb-3 img-fluid">

      <h1 class="fw-bold">ZING CRM</h1>

      <p class="subtitle">
        Manage Customers.<br>
        Grow Relationships.
      </p>
    </div>
    <form [formGroup]="form" (ngSubmit)="submit()">

  <div class="input-group-modern">
    <span>👤</span>
    <input
      type="text"
      class="input-modern"
      placeholder="Username"
      formControlName="username">
  </div>

  <div class="input-group-modern mt-3">
    <span>🔒</span>
    <input
      type="password"
      class="input-modern"
      placeholder="Password"
      formControlName="password">
  </div>

  <div *ngIf="error" class="error-message mt-3">
      {{ error }}
  </div>

  <button
      class="btn-modern w-100 mt-4"
      type="submit"
      [disabled]="form.invalid">

      Login →
  </button>
  <button
    *ngIf="pwa.canInstall()"
    type="button"
    class="btn btn-outline-light w-100 mt-3"
    (click)="pwa.installPwa()">

    📲 Install ZING CRM

</button>

</form>

  </div>

</div>
  `
})
export class LoginComponent {
  form: FormGroup;
  error = '';

constructor(
  private fb: FormBuilder,
  private auth: AuthService,
  private router: Router,
  public pwa: PwaService
)  {
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  submit() {
  console.log("Login button clicked");

  this.error = '';

  this.auth.login(
    this.form.value.username,
    this.form.value.password
  ).subscribe({
    next: res => {
      console.log(res);

      if (res.success) {
        this.router.navigate(['/dashboard']);
      } else {
        this.error = res.message;
      }
    },
    error: err => {
      console.log(err);
      this.error = 'Login failed';
    }
  });
}
}


// ================= DASHBOARD =================
// ================= DASHBOARD =================
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h3 class="mb-4">Dashboard</h3>

    <div class="container-fluid">

      <div class="row g-4">

        <div class="col-lg-3 col-md-6">
          <div class="crm-card stat-card">
            <div class="icon">👥</div>
            <h5>Total Leads</h5>
            <h2>{{stats.totalLeads}}</h2>
            <small>+12% this month</small>
          </div>
        </div>

        <div class="col-lg-3 col-md-6">
          <div class="crm-card stat-card">
            <div class="icon">🔥</div>
            <h5>Hot Customers</h5>
            <h2>{{stats.hotCustomers}}</h2>
            <small>Priority Leads</small>
          </div>
        </div>

        <div class="col-lg-3 col-md-6">
          <div class="crm-card stat-card">
            <div class="icon">📅</div>
            <h5>Today's Followups</h5>
            <h2>{{stats.todaysFollowups}}</h2>
            <small>Scheduled Today</small>
          </div>
        </div>

        <div class="col-lg-3 col-md-6">
          <div class="crm-card stat-card">
            <div class="icon">✅</div>
            <h5>Closed Deals</h5>
            <h2>{{stats.closedDeals}}</h2>
            <small>Completed</small>
          </div>
        </div>

      </div>

      <div class="row mt-5">

        <div class="col-lg-8">

          <div class="crm-card p-4">

            <h4 class="mb-4">Monthly Leads</h4>

            <canvas id="monthlyLeadsChart"></canvas>

          </div>

        </div>

      </div>

    </div>
  `
})
export class DashboardComponent implements OnInit, AfterViewInit {

  stats: DashboardStats = {
    totalLeads: 0,
    todaysFollowups: 0,
    pendingFollowups: 0,
    hotCustomers: 0,
    closedDeals: 0
  };

  constructor(private api: ApiService) {}

 ngOnInit() {

  this.api.getDashboardStats().subscribe(s => {
    this.stats = s;
  });

  this.api.getMonthlyLeadStats().subscribe(data => {

    const monthNames = [
      '',
      'Jan','Feb','Mar','Apr','May','Jun',
      'Jul','Aug','Sep','Oct','Nov','Dec'
    ];

    const labels = data.map(item => monthNames[item[0]]);
    const values = data.map(item => item[1]);

    this.createChart(labels, values);

  });

}

  ngAfterViewInit() {
  }

  createChart(labels: string[], values: number[]) {

  new Chart('monthlyLeadsChart', {

    type: 'bar',

    data: {

      labels,

      datasets: [
        {
          label: 'Monthly Leads',
          data: values,
          backgroundColor: '#2563eb',
          borderRadius: 8
        }
      ]

    },

    options: {

      responsive: true,

      plugins: {
        legend: {
          display: false
        }
      }

    }

  });

}

}
// ================= LEAD TYPES =================
@Component({
  selector: 'app-lead-type',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <h3>Lead Types</h3>
    <form [formGroup]="form" (ngSubmit)="save()" class="row g-2 mb-3">
      <div class="col-md-3"><input class="form-control" placeholder="Name" formControlName="name"></div>
      <div class="col-md-5"><input class="form-control" placeholder="Description" formControlName="description"></div>
      <div class="col-md-2">
        <button class="btn btn-primary w-100" [disabled]="form.invalid">{{ editingId ? 'Update' : 'Add' }}</button>
      </div>
      <div class="col-md-2" *ngIf="editingId">
        <button type="button" class="btn btn-secondary w-100" (click)="cancel()">Cancel</button>
      </div>
    </form>

    <table class="table table-striped table-sm bg-white">
      <thead><tr><th>Name</th><th>Description</th><th>Actions</th></tr></thead>
      <tbody>
        <tr *ngFor="let lt of leadTypes">
          <td>{{ lt.name }}</td>
          <td>{{ lt.description }}</td>
          <td>
            <button class="btn btn-sm btn-outline-primary me-1" (click)="edit(lt)">Edit</button>
            <button class="btn btn-sm btn-outline-danger" (click)="remove(lt)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  `
})
export class LeadTypeComponent implements OnInit {
  leadTypes: LeadType[] = [];
  form: FormGroup;
  editingId?: number;

  constructor(private fb: FormBuilder, private api: ApiService) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: ['']
    });
  }

  ngOnInit() { this.load(); }
  load() { this.api.getLeadTypes().subscribe(list => this.leadTypes = list); }

  save() {
    const val = this.form.value;
    const req = this.editingId ? this.api.updateLeadType(this.editingId, val) : this.api.createLeadType(val);
    req.subscribe(() => { this.cancel(); this.load(); });
  }

  edit(lt: LeadType) {
    this.editingId = lt.id;
    this.form.patchValue({ name: lt.name, description: lt.description });
  }

  cancel() { this.editingId = undefined; this.form.reset(); }

  remove(lt: LeadType) {
    if (confirm(`Delete lead type "${lt.name}"?`)) this.api.deleteLeadType(lt.id!).subscribe(() => this.load());
  }
}

// ================= CUSTOMER LEADS (list + search/filter + form + followups + notes) =================
@Component({
  selector: 'app-customer-lead',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <h3>Customer Leads</h3>

    <!-- Search & filter -->
    <form [formGroup]="filterForm" (ngSubmit)="doSearch()" class="row g-2 mb-3 bg-white p-2 rounded shadow-sm">
      <div class="col-md-2"><input class="form-control form-control-sm" placeholder="Name" formControlName="name"></div>
      <div class="col-md-2"><input class="form-control form-control-sm" placeholder="Mobile" formControlName="mobile"></div>
      <div class="col-md-2">
        <select class="form-select form-select-sm" formControlName="leadTypeId">
          <option [ngValue]="null">All Lead Types</option>
          <option *ngFor="let lt of leadTypes" [ngValue]="lt.id">{{ lt.name }}</option>
        </select>
      </div>
      <div class="col-md-2">
        <select class="form-select form-select-sm" formControlName="status">
          <option [ngValue]="null">All Status</option>
          <option *ngFor="let s of statusOptions" [ngValue]="s">{{ s }}</option>
        </select>
      </div>
      <div class="col-md-2">
        <select class="form-select form-select-sm" formControlName="priority">
          <option [ngValue]="null">All Priority</option>
          <option *ngFor="let p of priorityOptions" [ngValue]="p">{{ p }}</option>
        </select>
      </div>
      <div class="col-md-2"><input class="form-control form-control-sm" placeholder="City" formControlName="city"></div>
      <div class="col-md-2"><input type="date" class="form-control form-control-sm" formControlName="fromDate"></div>
      <div class="col-md-2"><input type="date" class="form-control form-control-sm" formControlName="toDate"></div>
      <div class="col-md-2"><button class="btn btn-sm btn-primary w-100">Search</button></div>
      <div class="col-md-2"><button type="button" class="btn btn-sm btn-secondary w-100" (click)="clearSearch()">Clear</button></div>
      <div class="col-md-2"><button type="button" class="btn btn-sm btn-success w-100" (click)="newLead()">+ New Lead</button></div>
    </form>

    <div class="row">
      <!-- List -->
      <div class="col-md-7">
        <table class="table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Mobile</th>
              <th>Type</th>
              <th>Status</th>
              <th>Priority</th>
              <th>Next Follow-up</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let l of leads" class="clickable" [class.table-active]="selected?.id === l.id" (click)="select(l)">
              <td>{{ l.customerName }}</td>
              <td>{{ l.mobile }}</td>
              <td>{{ l.leadType?.name }}</td>
              
              <!-- 1. GLOWING STATUS BADGES -->
              <td>
                <span class="badge-custom" [ngClass]="{
                  'badge-won': l.status === 'Closed Won',
                  'badge-hot': l.status === 'Interested' || l.status === 'New',
                  'badge-warm': l.status === 'Follow Up' || l.status === 'Contacted' || l.status === 'Visit Scheduled',
                  'badge-cold': l.status === 'Not Interested' || l.status === 'Closed Lost'
                }">
                  {{ l.status }}
                </span>
              </td>
              
              <!-- 2. GLOWING PRIORITY BADGES -->
              <td>
                <span class="badge-custom" [ngClass]="{
                  'badge-hot': l.priority === 'Hot', 
                  'badge-warm': l.priority === 'Warm', 
                  'badge-cold': l.priority === 'Cold', 
                  'bg-secondary text-white': l.priority === 'Not a Customer'
                }">
                  {{ l.priority }}
                </span>
              </td>
              
              <td>{{ l.nextFollowupDate }}</td>
              <td>
                <button class="btn btn-sm btn-outline-danger" (click)="remove(l, $event)">Del</button>
              </td>
            </tr>
            <tr *ngIf="leads.length === 0">
              <td colspan="7" class="text-center text-muted">No leads found</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Detail: form + followups + notes -->
      <div class="col-md-5" *ngIf="showForm">
        <div class="card p-3 mb-3">
          <h5>{{ form.value.id ? 'Edit Lead' : 'New Lead' }}</h5>
          <form [formGroup]="form" (ngSubmit)="saveLead()">
            <input class="form-control form-control-sm mb-2" placeholder="Customer Name*" formControlName="customerName">
            <input class="form-control form-control-sm mb-2" placeholder="Mobile*" formControlName="mobile">
            <input class="form-control form-control-sm mb-2" placeholder="Alternate Number" formControlName="alternateNumber">
            <input class="form-control form-control-sm mb-2" placeholder="Email" formControlName="email">
            <select class="form-select form-select-sm mb-2" formControlName="leadTypeId">
              <option [ngValue]="null">Select Lead Type</option>
              <option *ngFor="let lt of leadTypes" [ngValue]="lt.id">{{ lt.name }}</option>
            </select>
            <input class="form-control form-control-sm mb-2" placeholder="City" formControlName="city">
            <input class="form-control form-control-sm mb-2" placeholder="Address" formControlName="address">
            <textarea class="form-control form-control-sm mb-2" placeholder="Requirement" formControlName="requirement"></textarea>
            <input class="form-control form-control-sm mb-2" placeholder="Lead Source" formControlName="leadSource">
            <input class="form-control form-control-sm mb-2" placeholder="Assigned Executive" formControlName="assignedExecutive">
            <textarea class="form-control form-control-sm mb-2" placeholder="Discussion Details" formControlName="discussionDetails"></textarea>
            <label class="form-label small mb-0">Visit Date</label>
            <input type="date" class="form-control form-control-sm mb-2" formControlName="visitDate">
            <label class="form-label small mb-0">Next Follow-up Date</label>
            <input type="date" class="form-control form-control-sm mb-2" formControlName="nextFollowupDate">
            <select class="form-select form-select-sm mb-2" formControlName="status">
              <option *ngFor="let s of statusOptions" [ngValue]="s">{{ s }}</option>
            </select>
            <select class="form-select form-select-sm mb-2" formControlName="priority">
              <option [ngValue]="null">Priority</option>
              <option *ngFor="let p of priorityOptions" [ngValue]="p">{{ p }}</option>
            </select>
            <button class="btn btn-primary btn-sm w-100" [disabled]="form.invalid">Save</button>
          </form>
        </div>

        <div class="card p-3 mb-3" *ngIf="selected?.id">
          <h6>Follow-up History</h6>
          <form [formGroup]="followupForm" (ngSubmit)="addFollowup()" class="d-flex gap-1 mb-2">
            <input type="date" class="form-control form-control-sm" formControlName="followupDate">
            <input class="form-control form-control-sm" placeholder="Remarks" formControlName="remarks">
            <button class="btn btn-sm btn-outline-primary">Add</button>
          </form>
          <ul class="list-group list-group-flush small">
            <li class="list-group-item" *ngFor="let f of followups">
              <b>{{ f.followupDate }}</b> — {{ f.remarks }}
            </li>
            <li class="list-group-item text-muted" *ngIf="followups.length===0">No follow-ups yet</li>
          </ul>
        </div>

        <div class="card p-3" *ngIf="selected?.id">
          <h6>Notes</h6>
          <form [formGroup]="noteForm" (ngSubmit)="addNote()" class="d-flex gap-1 mb-2">
            <input class="form-control form-control-sm" placeholder="Add a note" formControlName="noteText">
            <button class="btn btn-sm btn-outline-primary">Add</button>
          </form>
          <ul class="list-group list-group-flush small">
            <li class="list-group-item d-flex justify-content-between" *ngFor="let n of notes">
              <span>{{ n.noteText }}</span>
              <button class="btn btn-sm btn-link text-danger p-0" (click)="removeNote(n)">x</button>
            </li>
            <li class="list-group-item text-muted" *ngIf="notes.length===0">No notes yet</li>
          </ul>
        </div>
      </div>
    </div>
  `
})
export class CustomerLeadComponent implements OnInit {
  leads: CustomerLead[] = [];
  leadTypes: LeadType[] = [];
  selected?: CustomerLead;
  showForm = false;
  statusOptions = STATUS_OPTIONS;
  priorityOptions = PRIORITY_OPTIONS;

  form: FormGroup;
  filterForm: FormGroup;
  followupForm: FormGroup;
  noteForm: FormGroup;

  followups: FollowUp[] = [];
  notes: Note[] = [];

  constructor(private fb: FormBuilder, private api: ApiService) {
    this.form = this.fb.group({
      id: [null],
      customerName: ['', Validators.required],
      mobile: ['', Validators.required],
      alternateNumber: [''],
      email: [''],
      leadTypeId: [null],
      city: [''],
      address: [''],
      requirement: [''],
      leadSource: [''],
      assignedExecutive: [''],
      discussionDetails: [''],
      visitDate: [''],
      nextFollowupDate: [''],
      status: ['New'],
      priority: [null]
    });

    this.filterForm = this.fb.group({
      name: [''], mobile: [''], leadTypeId: [null], status: [null],
      priority: [null], city: [''], fromDate: [''], toDate: ['']
    });

    this.followupForm = this.fb.group({ followupDate: ['', Validators.required], remarks: ['', Validators.required] });
    this.noteForm = this.fb.group({ noteText: ['', Validators.required] });
  }

  ngOnInit() {
    this.api.getLeadTypes().subscribe(lt => this.leadTypes = lt);
    this.loadLeads();
  }

  loadLeads() { this.api.getLeads().subscribe(l => this.leads = l); }

  doSearch() { this.api.searchLeads(this.filterForm.value).subscribe(l => this.leads = l); }
  clearSearch() { this.filterForm.reset(); this.loadLeads(); }

  newLead() {
    this.selected = undefined;
    this.form.reset({ status: 'New' });
    this.showForm = true;
    this.followups = [];
    this.notes = [];
  }

  select(l: CustomerLead) {
    this.selected = l;
    this.showForm = true;
    this.form.patchValue({ ...l, leadTypeId: l.leadType?.id ?? null });
    this.api.getFollowups(l.id!).subscribe(f => this.followups = f);
    this.api.getNotes(l.id!).subscribe(n => this.notes = n);
  }

  saveLead() {
    const val = this.form.value;
    const req = val.id ? this.api.updateLead(val.id, val) : this.api.createLead(val);
    req.subscribe(saved => { this.loadLeads(); this.select(saved); });
  }

  remove(l: CustomerLead, ev: Event) {
    ev.stopPropagation();
    if (confirm(`Delete lead "${l.customerName}"?`)) {
      this.api.deleteLead(l.id!).subscribe(() => { this.loadLeads(); if (this.selected?.id === l.id) this.showForm = false; });
    }
  }

  addFollowup() {
    if (!this.selected?.id) return;
    const payload = { ...this.followupForm.value, statusAtFollowup: this.form.value.status, nextFollowupDate: this.followupForm.value.followupDate };
    this.api.addFollowup(this.selected.id, payload).subscribe(() => {
      this.followupForm.reset();
      this.api.getFollowups(this.selected!.id!).subscribe(f => this.followups = f);
      this.loadLeads();
    });
  }

  addNote() {
    if (!this.selected?.id) return;
    this.api.addNote(this.selected.id, this.noteForm.value).subscribe(() => {
      this.noteForm.reset();
      this.api.getNotes(this.selected!.id!).subscribe(n => this.notes = n);
    });
  }

  removeNote(n: Note) {
    this.api.deleteNote(n.id!).subscribe(() => this.api.getNotes(this.selected!.id!).subscribe(list => this.notes = list));
  }
}

// ================= REMINDERS =================
@Component({
  selector: 'app-reminders',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h3>Today's & Overdue Follow-ups</h3>
    <table class="table table-sm table-striped bg-white">
      <thead><tr><th>Name</th><th>Mobile</th><th>Status</th><th>Priority</th><th>Next Follow-up</th></tr></thead>
      <tbody>
        <tr *ngFor="let l of due">
          <td>{{ l.customerName }}</td>
          <td>{{ l.mobile }}</td>
          <td>{{ l.status }}</td>
          <td>{{ l.priority }}</td>
          <td [class.text-danger]="l.nextFollowupDate! < today">{{ l.nextFollowupDate }}</td>
        </tr>
        <tr *ngIf="due.length === 0"><td colspan="5" class="text-center text-muted">Nothing due 🎉</td></tr>
      </tbody>
    </table>
  `
})
export class RemindersComponent implements OnInit {
  due: CustomerLead[] = [];
  today = new Date().toISOString().slice(0, 10);
  constructor(private api: ApiService) {}
  ngOnInit() { this.api.getReminders().subscribe(l => this.due = l); }
}
