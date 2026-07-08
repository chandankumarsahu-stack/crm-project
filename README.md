# Customer Lead CRM System

Angular 17 + Spring Boot 3 + MySQL. Built with the minimum number of files needed to
cover every module in the assignment (login, lead types, customer leads, follow-ups
with history, notes, dashboard, search/filter, and a reminders page).

## File map (25 files total)

**Backend** (`/backend`) — 8 files, single flat package `com.crm`:
- `pom.xml`, `application.properties`
- `CrmApplication.java` — main class, CORS config, seeds `admin/admin123`
- `Entities.java` — all 5 JPA entities (users, lead_type, customer_lead, follow_up, notes)
- `Repositories.java` — all Spring Data repositories
- `Dtos.java` — all request/response DTOs
- `Services.java` — layered business logic (Auth, LeadType, CustomerLead, FollowUp, Note services) + dynamic search Specification + custom exception
- `Controllers.java` — all REST controllers + global exception handler

**Frontend** (`/frontend`) — 14 files, Angular 17 standalone components (no NgModules):
- `angular.json`, `package.json`, `tsconfig.json`, `tsconfig.app.json`
- `src/index.html`, `src/styles.css`, `src/main.ts`
- `src/app/app.config.ts`, `app.routes.ts`, `app.component.ts` (nav bar)
- `src/app/models.ts` — all TypeScript interfaces
- `src/app/api.service.ts` — AuthService + ApiService (every HTTP call)
- `src/app/auth.guard.ts`
- `src/app/pages.ts` — Login, Dashboard, Lead Type, Customer Lead (with search/filter, follow-up history, notes), and Reminders components

**Root**: `schema.sql` (reference — tables auto-created by Hibernate), `postman_collection.json`

## Run it

### 1. MySQL
Create nothing manually — just have MySQL running. Update credentials in
`backend/src/main/resources/application.properties` if needed. The app auto-creates
`crm_db` and all 5 tables on first run, and seeds a default login (`admin` / `admin123`).

### 2. Backend
```
cd backend
mvn spring-boot:run
```
Runs on `http://localhost:8080`.

### 3. Frontend
```
cd frontend
npm install
ng serve
```
Runs on `http://localhost:4200`.

## What's covered vs. the spec
- **Login**: plain username/password check against the `users` table, no Spring Security.
- **Lead Type Management**: full CRUD, unlimited types.
- **Customer Lead Management**: all listed fields, CRUD.
- **Follow-up Management**: every follow-up saved to `follow_up` table (full history per lead), keeps the lead's status/next-followup-date in sync.
- **Dashboard**: total leads, today's follow-ups, pending (overdue) follow-ups, hot customers, closed deals.
- **Search & Filter**: by lead type, status, priority, city, date range, name, mobile — built with a dynamic JPA Specification.
- **Reminders page**: today's + overdue follow-ups not yet closed.
- **Notes module**: unlimited notes per lead, add/delete.
- **Angular requirements**: standalone components, Router, Reactive Forms, HttpClient, Bootstrap (via CDN).
- **Spring Boot requirements**: REST APIs, Spring Data JPA, MySQL, DTOs, exception handling (`@RestControllerAdvice`), validation-ready, layered architecture (Controller → Service → Repository).

## Not implemented (bonus features)
Excel/PDF export, charts, pagination/sorting UI, dark mode, and WhatsApp button were left
out to keep the file count minimal — they're explicitly marked "Bonus" in the spec. Happy
to add any of them if you want — each is a small, isolated addition (e.g. Excel export is
one new controller endpoint + one new service method using Apache POI).
