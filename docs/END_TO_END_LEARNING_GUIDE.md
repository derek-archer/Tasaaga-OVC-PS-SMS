# TASAAGA SCHOOL MANAGEMENT SYSTEM (TSMS)
## Master End-to-End Application Learning Guide & Developer Handbook

---

### 📌 About Tasaaga Primary School
- **Location**: Sitabaale, Kiwenda, Wakiso District, Uganda
- **Motto**: *"Rising To Succeed"*
- **Official Website**: [tasaagaschool.org](https://www.tasaagaschool.org/)
- **Core Mission**: Providing top-tier inclusive primary education, day & boarding care, daily meals, healthcare, and life skills to over 200 orphaned and vulnerable children (OVC) annually.

---

## 🏛️ 1. Architecture & Design Patterns

TSMS is built following **Clean Architecture** and **Unidirectional Data Flow (UDF)** using modern Android Jetpack standards.

### Data Flow Diagram:
```
┌────────────────────────────────────────────────────────┐
│                      UI LAYER                          │
│  Jetpack Compose + Material 3 Brand Tokens             │
│  (HomeScreen, LoginScreen, 7 Role Dashboards, Support) │
└───────────────────────────▲────────────────────────────┘
                            │ (StateFlow / UI State)
┌───────────────────────────┴────────────────────────────┐
│                    VIEWMODEL LAYER                     │
│  Coroutines Flow, Validation, Auto Grade Calculations  │
│  (LoginViewModel, NewsViewModel, VolunteerViewModel)   │
└───────────────────────────▲────────────────────────────┘
                            │ (Repositories Interface)
┌───────────────────────────┴────────────────────────────┐
│                   REPOSITORY LAYER                     │
│  AppContainer Dependency Injection Singleton          │
│  UserRepository, NewsRepository, VolunteerRepository  │
└─────────────────────▲───────────▲──────────────────────┘
                      │           │
       (Postgrest REST)           (Local Default Fallbacks)
                      │           │
       ┌──────────────┴──┐     ┌──┴──────────────┐
       │ Supabase Remote │     │  Local Default  │
       │ PostgreSQL DB   │     │  Kotlin Objects │
       └─────────────────┘     └─────────────────┘
```

---

## 🔑 2. User Roles & Demo Credentials Matrix

TSMS enforces Role-Based Access Control (RBAC) across 7 distinct access roles. All demo accounts use default password **`Password123!`**:

| Role Icon | Role Name | Demo Email Address | Dashboard Composable | Key Capabilities |
| :-: | :--- | :--- | :--- | :--- |
| 🔧 | **Admin** | `admin@tasaagaschool.org` | `AdminDashboardScreen` | Student registration (`TAS-2026-xxx`), system config, audit logs |
| 👔 | **Headteacher** | `headteacher@tasaagaschool.org` | `HeadteacherDashboardScreen` | Exam results approval, expense approvals, attendance trend monitoring |
| 👩‍🏫 | **Teacher** | `teacher@tasaagaschool.org` | `TeacherDashboardScreen` | Attendance register updating, exam marks entry & auto grade calculation |
| 💳 | **Finance** | `finance@tasaagaschool.org` | `FinanceDashboardScreen` | Fee payments & auto receipt generation (`RCP-2026-xxxx`), expense requisitions |
| 👨‍👩‍👧 | **Parent** | `parent@tasaagaschool.org` | `ParentDashboardScreen` | Child progress tracking, class position ranking, official term report card |
| 🏘️ | **Boarding** | `boarding@tasaagaschool.org` | `BoardingDashboardScreen` | African animal dormitory bed allocation, welfare/health incident logging |
| 👦 | **Student** | `student@tasaagaschool.org` | `StudentDashboardScreen` | Personal timetable, term result scores & subject breakdown |

---

## 🏠 3. Public Pre-Login Experience & Page Structure

The pre-login homepage ([`HomeScreen.kt`](file:///C:/Users/derri/AndroidStudioProjects/TasaagaOVCPS/app/src/main/java/com/example/tasaagaovcps/ui/screens/HomeScreen.kt)) features a 9-part structured composition:

1. **Header Banner**: School logo, tagline *"Rising To Succeed"*, and a prominent yellow **[ 👤 Portal login ]** CTA button.
2. **Hero Banner**: *"A brighter future starts here."* with **Explore Our School** & **Admissions Enquiry** buttons + campus photograph.
3. **Welcome & Quote Card**: Gold vertical accent bar + quote *"Every child deserves a chance to learn, belong and succeed."*
4. **Our Programmes Grid**: Interactive 4-card grid (Primary Education, Day & Boarding, Meals & Wellbeing, Life Skills).
5. **Admissions Journey Flow**: 4-Step Timeline (`1 Enquire` → `2 Visit` → `3 Apply` → `4 Review`) + *"Ask about admissions"* CTA.
6. **Help a Child Learn & Thrive**: 3 Support action cards + *"Discuss how to help"* CTA.
7. **Database-Driven News & Notices**: Live news items dynamically queried from Supabase `news_items` database table via `NewsViewModel`.
8. **FAQ Accordion & Live Inquiry Form**: Expandable questions + *"Let's talk"* inquiry form with regex validation and confirmation dialog.
9. **Brand Footer**: Location info (*Sitabaale, Uganda \| Day & Boarding*) + Quick Links.

---

## 🗄️ 4. Supabase Database Schema (22 PostgreSQL Tables)

The backend consists of 22 PostgreSQL tables configured in [`docs/supabase/schema.sql`](file:///C:/Users/derri/AndroidStudioProjects/TasaagaOVCPS/docs/supabase/schema.sql):

- **Profiles & Users**: `profiles`
- **School & Academic Structure**: `school_info`, `academic_years`, `terms`, `classes`, `subjects`
- **Student & Guardian Directory**: `students`, `parents`
- **Attendance Register**: `attendance`
- **Finance & Fee Collections**: `fee_structures`, `payments`, `expenses`, `donation_info`
- **African Animal Boarding Dormitories**: `boarding_dorms` (`Dormitory Lion`, `Elephant`, `Giraffe`, `Zebra`), `boarding_allocations`, `boarding_welfare`
- **Staff & Inventory**: `staff`, `inventory_assets`, `inventory_stock`, `inventory_movements`
- **Communication & Audit**: `announcements`, `news_items`, `volunteer_opportunities`, `audit_logs`

### Direct DDL Execution via `exec_sql` RPC:
```sql
CREATE OR REPLACE FUNCTION public.exec_sql(sql_query TEXT)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
BEGIN
    EXECUTE sql_query;
END;
$$;
```

---

## 📊 5. Interactive Dashboard Workflows & Grade Calculations

### Teacher Exam Marks Entry & Automated Grade Logic:
```kotlin
data class StudentResultEntry(
    val studentName: String,
    val maths: Int,
    val english: Int,
    val science: Int,
    val sst: Int,
    val re: Int
) {
    val total: Int get() = maths + english + science + sst + re
    val average: Double get() = total / 5.0
    val grade: String get() = when {
        average >= 80 -> "D1 (Distinction)"
        average >= 70 -> "D2 (Distinction)"
        average >= 60 -> "C3 (Credit)"
        average >= 50 -> "C4 (Credit)"
        else -> "P7 (Pass)"
    }
}
```

---

## 🚀 6. Developer Onboarding & Commands

### Build Commands:
```bash
# Run All Unit Tests
./gradlew app:testDebugUnitTest

# Assemble Debug APK
./gradlew app:assembleDebug
```

### Generated APK Output:
`app/build/outputs/apk/debug/app-debug.apk` *(38.9 MB)*
