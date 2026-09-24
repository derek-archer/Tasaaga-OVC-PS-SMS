# TASAAGA SCHOOL MANAGEMENT SYSTEM (TSMS)
## Software Requirements Specification (SRS)
**End-to-End Android, Web Administration, and Supabase Backend Platform**

---

### Document Control
- **Organization**: Tasaaga Primary School, Sitabaale, Uganda
- **System Name**: Tasaaga School Management System (TSMS)
- **Motto**: *Rising To Succeed*
- **Official Website**: [https://www.tasaagaschool.org/](https://www.tasaagaschool.org/)
- **Document Version**: 1.1
- **Primary Platform**: Android Application (Jetpack Compose & Material 3) with Web Administration Portal
- **Backend Architecture**: Supabase PostgreSQL + Postgrest API + Supabase Auth
- **Prepared Date**: 2026-09-21

---

## 1. Executive Summary & Introduction

### 1.1 Purpose
The Tasaaga School Management System (TSMS) is a unified digital platform designed to digitize and manage student records, academic operations, fee collections, staff activities, boarding services, community outreach, and executive reporting for Tasaaga Primary School in Sitabaale, Uganda.

### 1.2 System Scope
The platform provides role-aware access via an Android mobile application built with Jetpack Compose and a browser-based web administration portal. The backend utilizes Supabase (PostgreSQL, Auth, Postgrest) and Resend API for external communications.

---

## 2. Stakeholders & Access Roles

TSMS enforces Role-Based Access Control (RBAC) across 7 distinct user roles:

| Role | Description & Scope | Primary Screen / Portal |
| :--- | :--- | :--- |
| **Super Admin** | Full system configuration, user management, audit logs, and settings. | `AdminDashboardScreen` |
| **Headteacher** | Academic oversight, approvals for exam results and expenses, attendance trends. | `HeadteacherDashboardScreen` |
| **Teacher** | Class attendance recording, assessment marks entry, timetable viewing. | `TeacherDashboardScreen` |
| **Finance Officer** | Fee structure management, payment recording (Cash/MoMo/Bank), receipts, expenses. | `FinanceDashboardScreen` |
| **Parent / Guardian** | Child progress tracking, attendance view, approved results, fee balances, announcements. | `ParentDashboardScreen` |
| **Boarding Officer** | Dormitory occupancy, bed allocations, boarding welfare incident logging. | `BoardingDashboardScreen` |
| **Student** | Class results, personal timetable, attendance record, school announcements. | `StudentDashboardScreen` |

---

## 3. Functional Requirements

### 3.1 Student & Guardian Management
- Unique Admission Numbering: Auto-generation of unique IDs (`TAS-2026-xxx`).
- Student Categorization: Day vs. Boarding, Gender, Class & Stream assignment (`P.1A` to `P.7B`).
- Guardian Linking: Link one or more parents/guardians with contact details and phone numbers (`+2567xxxxxxxx`).

### 3.2 Attendance Tracking
- Daily Attendance: Record student attendance status (`Present`, `Absent`, `Late`, `Excused`) with timestamp.
- Duplicate Prevention: Enforce one primary attendance record per student per date.

### 3.3 Academics, Assessments & Results
- Core & Optional Subjects: Mathematics, English, Science, Social Studies (SST), Religious Education (RE), Kiswahili, Physical Education, Art & Craft.
- Grading Workflow: Teachers enter marks in `Draft` state -> Submit for Review -> Headteacher Approval -> Published to Parents and Students.
- Position & Averages: Automated class ranking and average percentage calculation.

### 3.4 Finance, Fees & Payments
- Fee Structures: Configure fee categories separately for Day Students (Tuition, Development, Medical, Activity) and Boarding Students (Boarding, Meals, Tuition, Medical, Activity).
- Payment Processing: Record payments via Cash, MTN Mobile Money (`MTN-xxxx`), Airtel Money (`ATL-xxxx`), or Bank Transfer with auto-generated unique receipts (`RCP-2026-xxxx`).
- Payment Reversals: Reversals require administrative approval and preserve full audit history.

### 3.5 Boarding & Welfare
- Dormitory Management: Dormitory capacities, gender division (Dorm A/B Boys, Dorm C/D Girls), and occupancy tracking.
- Bed Allocation & Welfare: Room/bed assignment and health/discipline incident logging (`Active` / `Resolved`).

### 3.6 Communication & Community Outreach
- School Announcements: Target announcements to `All`, `Parents`, `Students`, or `Staff` with `Draft` / `Published` states.
- Public Outreach & Volunteering: Expose community news (`CLINIC`, `COMMUNITY`, `SUCCESS_STORY`) and volunteer applications via Resend Email API.

### 3.7 Audit Trail & Logging
- System-Wide Auditing: Every critical action (payment confirmation, mark submission, expense approval, student registration) logs timestamp, actor, entity, action type, and IP address.

---

## 4. Supabase Database Schema

```sql
-- Profiles Table
CREATE TABLE public.profiles (
    id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    role VARCHAR(50) NOT NULL CHECK (role IN ('Admin', 'Headteacher', 'Teacher', 'Finance', 'Parent', 'Boarding', 'Student')),
    email VARCHAR(255),
    name VARCHAR(255),
    phone VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- News Items Table
CREATE TABLE public.news_items (
    id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(50) NOT NULL CHECK (category IN ('CLINIC', 'COMMUNITY', 'SUCCESS_STORY')),
    date VARCHAR(50) NOT NULL,
    image_url TEXT
);

-- Volunteer Opportunities Table
CREATE TABLE public.volunteer_opportunities (
    id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    requirements TEXT[] DEFAULT '{}',
    duration VARCHAR(100) NOT NULL,
    accommodation TEXT NOT NULL
);

-- School Info Table
CREATE TABLE public.school_info (
    id INT PRIMARY KEY DEFAULT 1,
    mission TEXT NOT NULL,
    motto VARCHAR(255) NOT NULL,
    programs TEXT[] DEFAULT '{}'
);

-- Donation Info Table
CREATE TABLE public.donation_info (
    id INT PRIMARY KEY DEFAULT 1,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    donation_methods TEXT[] DEFAULT '{}',
    sponsorship_details TEXT NOT NULL
);
```

---

## 5. Non-Functional & Technical Requirements

- **Framework**: Jetpack Compose with Material 3 & Navigation 3 (`androidx.navigation3`).
- **Resilience & Offline Support**: Repositories expose `StateFlow` backed by Supabase Postgrest queries with fallback to local defaults if offline or if database tables are unpopulated.
- **Serialization**: Kotlinx Serialization (`@Serializable` / `@SerialName`) for type-safe JSON decoding from Postgrest.
- **Security**: Row Level Security (RLS) on Supabase PostgreSQL tables and secure API key management.

---

## 6. Hardware & Donor Budget Summary

### 6.1 Hardware Equipment Plan (UGX 30,500,000)
- 4 Administration Laptops (UGX 10,000,000)
- 10 Android Smartphones for Teachers/Staff (UGX 5,500,000)
- 1 Desktop PC, 1 Network Printer/Scanner, 4 UPS Units, Network Router/Switch/Wi-Fi APs, Solar/Inverter Backup.

### 6.2 Overall Project Budget (UGX 88,550,000)
- Software Development & Android App: UGX 28,000,000
- Hardware & Equipment: UGX 30,500,000
- Connectivity, Hosting & Domain: UGX 4,500,000
- Training & Change Management: UGX 5,000,000
- Data Migration & Cleansing: UGX 4,000,000
- Security & Support: UGX 8,500,000
- Contingency: UGX 8,050,000
