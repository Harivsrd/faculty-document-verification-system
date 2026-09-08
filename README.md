# Faculty Credential & Document Verification System

A full-stack portfolio project for managing faculty profiles, educational
qualifications, teaching/work experience, and administrative document
verification.

**Backend:** Java 17, Spring Boot 3, Spring Security + JWT, Spring Data JPA,
MySQL, Maven
**Frontend:** React (Vite), Tailwind CSS, Axios, React Router

---

## Features

**Faculty**
- Register / login (JWT-based auth)
- Manage profile, education, and experience records (own records only)
- Upload certificates for education/experience; re-upload after rejection
- View document status, rejection reasons, and a profile-completion dashboard

**Admin**
- View / search / filter all faculty
- Preview and download uploaded certificates (never publicly exposed)
- Approve / reject documents with a required rejection reason
- View verification history per document and a full audit log
- Dashboard with faculty/document counts

**Public (optional)**
- Search and view approved faculty and their verified qualifications only

---

## Project Structure

```
faculty-credential-system/
├── backend/     Spring Boot REST API
└── frontend/    React (Vite) SPA
```

See `backend/src/main/java/com/example/faculty` for the layered structure
(`entity`, `repository`, `service`/`service/impl`, `controller`, `security`,
`config`, `dto`, `exception`, `mapper`).

---

## Backend Setup

### Prerequisites
- Java 17+
- Maven 3.9+
- MySQL 8+ running locally (or update the datasource URL)

### 1. Create the database
The app will auto-create the schema (`ddl-auto: update`) and the database
itself (`createDatabaseIfNotExist=true`) as long as MySQL is reachable and
the credentials in `application.yml` are correct.

### 2. Configure environment variables (recommended)
```
export DB_USERNAME=root
export DB_PASSWORD=your_mysql_password
export JWT_SECRET=$(openssl rand -base64 48)
export ADMIN_SEED_EMAIL=admin@yourcompany.com
export ADMIN_SEED_PASSWORD=ChangeMe123!
```
If you skip this, the defaults in `application.yml` are used — fine for
local development, **not** for anything you deploy.

### 3. Run
```bash
cd backend
mvn spring-boot:run
```
The API starts on `http://localhost:8080`.

### 4. First admin login
On first startup, `AdminSeeder` creates a single ADMIN account (if one
doesn't already exist) using `ADMIN_SEED_EMAIL` / `ADMIN_SEED_PASSWORD`
(defaults: `admin@faculty-system.local` / `Admin@12345`). Admin accounts are
**not** creatable through public registration — this seeder is the only
way to provision the first admin. Log in and treat that password as
temporary.

### Run tests
```bash
cd backend
mvn test
```

---

## Frontend Setup

### Prerequisites
- Node.js 18+

### 1. Install dependencies
```bash
cd frontend
npm install
```

### 2. Configure the API URL
```bash
cp .env.example .env
# edit VITE_API_BASE_URL if your backend isn't on localhost:8080
```

### 3. Run
```bash
npm run dev
```
The app starts on `http://localhost:5173` and talks to the backend at the
URL in `.env`.

---

## Key API Endpoints

| Area | Method & Path |
|---|---|
| Auth | `POST /api/auth/register`, `POST /api/auth/login` |
| Faculty profile | `GET/PUT /api/faculty/profile` |
| Education | `GET/POST /api/faculty/education`, `PUT/DELETE /api/faculty/education/{id}` |
| Experience | `GET/POST /api/faculty/experience`, `PUT/DELETE /api/faculty/experience/{id}` |
| Upload certs | `POST /api/faculty/education/{id}/certificate`, `POST /api/faculty/experience/{id}/certificate` |
| Faculty dashboard | `GET /api/faculty/dashboard` |
| Admin: faculty | `GET /api/admin/faculty`, `GET /api/admin/faculty/{id}` |
| Admin: verification | `GET /api/admin/documents/pending`, `PUT .../{id}/approve`, `PUT .../{id}/reject` |
| Admin: preview/download | `GET /api/admin/documents/{id}/preview`, `GET .../{id}/download` |
| Admin: history/audit | `GET /api/admin/documents/{id}/history`, `GET /api/admin/audit-logs` |
| Admin: dashboard | `GET /api/admin/dashboard` |
| Public directory | `GET /api/public/faculty`, `GET /api/public/faculty/{id}` |

---

## Security Notes

- Passwords are hashed with BCrypt; JWTs are HMAC-SHA256 signed and stored
  client-side in `localStorage`.
- Uploaded files are stored under `uploads/faculty-{id}/{category}/` with
  generated UUID filenames — the original filename is never trusted as a
  path, and traversal attempts are rejected.
- `/uploads/**` is never served as a static directory. Every document read
  goes through `/api/admin/documents/{id}/preview|download`, which checks
  JWT + `ROLE_ADMIN` before touching the filesystem.
- Faculty-side endpoints re-verify record ownership on every request
  (a faculty member cannot read/edit/delete another faculty member's
  education, experience, or documents just by guessing an ID).
- Admin accounts cannot be created through public registration.

## Known Limitations / Next Steps

- No refresh-token flow; the JWT simply expires after 24h and the user is
  redirected to `/login`.
- No email verification or password-reset flow.
- Admin document actions (`approve`/`reject`) are structured as
  single-document operations; there's no bulk-action UI yet.
