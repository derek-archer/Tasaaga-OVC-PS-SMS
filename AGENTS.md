# TSMS — Universal Agent Context Prompt
**For: Claude Code · Gemini Agent (Android Studio) · OpenAI Codex**
**Project: Tasaaga School Management System (TSMS)**
**Repo: https://github.com/derek-archer/Tasaaga-OVC-PS-SMS**

---

## 1. What This App Is

Tasaaga OVC Primary School SMS is a **Kotlin + Jetpack Compose Android application** that digitises school operations for Tasaaga Primary School in Sitabaale, Uganda. It serves 7 distinct user roles via a single app with role-based dashboards. All data is live from Supabase — nothing is hardcoded.

- **Official site**: https://www.tasaagaschool.org/
- **Motto**: *Rising To Succeed*
- **SRS document**: `docs/SRS.md`

---

## 2. Technology Stack (DO NOT CHANGE)

| Layer | Library | Version |
|---|---|---|
| Language | Kotlin | 2.0.21 |
| UI | Jetpack Compose + Material 3 | BOM 2024.09.00 |
| Navigation | Navigation 3 | `androidx.navigation3:1.0.1` |
| Backend | Supabase Kotlin SDK | `io.github.jan-tennert.supabase` v3.0.3 |
| Serialization | Kotlinx Serialization | `@Serializable` / `@SerialName` |
| AI | Google Gemini Flash | via `BuildConfig.GEMINI_API_KEY` |
| Architecture | MVVM + Repository Pattern + UDF | StateFlow / collectAsState() |

---

## 3. Project Structure

```
com.example.tasaagaovcps/
├── data/
│   ├── model/Models.kt          ← ALL data classes (@Serializable). One file.
│   ├── repository/              ← One interface + impl per domain
│   │   ├── UserRepository.kt
│   │   ├── StudentRepository.kt
│   │   ├── AttendanceRepository.kt
│   │   ├── ExamResultRepository.kt
│   │   ├── FinanceRepository.kt
│   │   ├── BoardingRepository.kt
│   │   ├── StaffRepository.kt
│   │   ├── AnnouncementRepository.kt
│   │   └── GeminiRepository.kt
│   └── AppContainer.kt          ← Wires all repositories with shared SupabaseClient
├── ui/
│   ├── components/              ← SummaryCard, GeminiInsightCard, GeminiChatBottomSheet
│   ├── navigation/TasaagaNavigation.kt   ← Typed data class routes
│   ├── screens/                 ← One screen per role + LoginScreen
│   ├── theme/                   ← TasaagaOVCPSTheme (Material 3)
│   └── viewmodel/
│       ├── AppViewModelProvider.kt       ← viewModelFactory{} for all VMs
│       ├── LoginViewModel.kt
│       ├── AdminViewModel.kt
│       ├── HeadteacherViewModel.kt
│       ├── TeacherViewModel.kt
│       ├── FinanceViewModel.kt
│       ├── BoardingViewModel.kt
│       ├── ParentViewModel.kt
│       └── StudentViewModel.kt
```

---

## 4. Supabase Backend

- **Project ID**: `dibxccfjbntfnzhqpcuv`
- **School UUID** (always filter by this): `a7081bf1-0283-4aee-be9d-b66f590bb1a1`
- **Auth**: Supabase Auth — profiles table linked to `auth.users` via UUID
- **RLS**: Enabled on ALL tables — never bypass with service role key in client code

### Live Tables (exact names — use these, not guesses)

| Table | Key Columns |
|---|---|
| `profiles` | `id` (UUID), `role`, `email`, `name`, `phone`, `school_id` |
| `students` | `id`, `name`, `adm_no`, `class_id`, `class_name`, `gender`, `student_type`, `parent_id` (UUID FK to profiles), `school_id`, `status` |
| `attendance` | `id`, `student_id`, `status`, `date`, `school_id` |
| `exam_results` | `id`, `student_id`, `student_name`, `class_id`, `subject`, `marks`, `max_marks`, `grade`, `term`, `academic_year`, `school_id` |
| `payments` | `id`, `receipt_no`, `student_id`, `student_name`, `class_id`, `amount`, `payment_method`, `payment_date`, `recorded_by`, `status`, `school_id` |
| `expenses` | `id`, `date`, `category`, `description`, `amount`, `requested_by`, `approved_by`, `status`, `school_id` |
| `fees` | `id`, `category`, `student_type`, `amount`, `term`, `status`, `school_id` |
| `announcements` | `id`, `title`, `body`, `audience`, `published_by`, `date`, `status`, `school_id` |
| `dormitories` | `id`, `name`, `gender`, `capacity`, `occupied` |
| `boarding_welfare` | `id`, `student_name`, `incident_type`, `description`, `date`, `reported_by`, `status` |
| `timetable` | `id`, `class_id`, `day`, `start_time`, `end_time`, `subject`, `teacher`, `school_id` |
| `staff` | `id`, `name`, `role`, `department`, `phone`, `email`, `joined_date`, `status` |

### DB Constraints to Respect
- `payments.payment_method` CHECK: only `Cash`, `MTN Mobile Money`, `Airtel Money`, `Bank Transfer`
- `boarding_welfare.incident_type` CHECK: only `Health`, `Discipline`, `General`
- `attendance.status` CHECK: only `Present`, `Absent`, `Late`, `Excused`
- `profiles.role` CHECK: only `Admin`, `Headteacher`, `Teacher`, `Finance`, `Parent`, `Boarding`, `Student`

---

## 5. The 7 Role Dashboards (SRS-Aligned)

Every dashboard MUST display live data from Supabase. No hardcoded strings, numbers, or lists.

| Role | Screen | ViewModel | Key Data Sources |
|---|---|---|---|
| Admin | `AdminDashboardScreen` | `AdminViewModel` | students, attendance, announcements |
| Headteacher | `HeadteacherDashboardScreen` | `HeadteacherViewModel` | students, attendance, exam_results, staff, announcements |
| Teacher | `TeacherDashboardScreen` | `TeacherViewModel` | attendance (by class), exam_results (by class), timetable, announcements |
| Finance | `FinanceDashboardScreen` | `FinanceViewModel` | payments, expenses, fees |
| Boarding | `BoardingDashboardScreen` | `BoardingViewModel` | dormitories, boarding_welfare, students (boarding type) |
| Parent | `ParentDashboardScreen` | `ParentViewModel` | student (own child), attendance, exam_results, payments, announcements |
| Student | `StudentDashboardScreen` | `StudentViewModel` | own profile, attendance, exam_results, timetable, announcements |

---

## 6. Navigation — Typed Routes

Routes are typed `data class` objects carrying IDs from login. Never navigate without `schoolId`.

```kotlin
// In TasaagaNavigation.kt
data class AdminDashboardRoute(val schoolId: String)
data class TeacherDashboardRoute(val schoolId: String, val classId: Int?)
data class ParentDashboardRoute(val schoolId: String, val studentId: Int?)
data class StudentDashboardRoute(val schoolId: String, val studentId: Int?, val classId: Int?)
// etc.
```

Login flow: `LoginViewModel.login()` → fetches `Profile` from Supabase → for Student/Parent roles also fetches `Student` via `StudentRepository.getStudentByProfile(profileId)` → emits `LoginUiState.Success(role, schoolId, studentId, classId)` → `TasaagaApp` navigates to the correct route.

---

## 7. Critical Rules — ALL Agents Must Follow

### ❌ NEVER DO
- Hardcode any data (student names, counts, amounts, class names)
- Store Gemini API key anywhere except `local.properties` — accessed ONLY via `BuildConfig.GEMINI_API_KEY`
- Use the Supabase service role key in client-side Kotlin code
- Bypass RLS or query without `school_id` filter
- Add new tables in Kotlin without a corresponding SQL migration in `docs/supabase/migrations/`
- Change the tech stack (no Room, no Hilt, no Retrofit beyond Resend email)
- Create a new `Models.kt` file — all data classes go in the single `data/model/Models.kt`
- Use `LazyColumn` inside a `Column` with `verticalScroll` — causes layout crashes

### ✅ ALWAYS DO
- Filter ALL Supabase queries by `school_id = 'a7081bf1-0283-4aee-be9d-b66f590bb1a1'`
- Make model fields nullable (`String?`, `Int?`) if the DB column allows NULL
- Use `displayName` computed property on `Student` (handles both `name` and `fname`+`lname`)
- Wrap ALL Supabase calls in `try/catch` — return `emptyList()` or `null` on error
- Add new repositories to `AppContainer.kt` AND `AppViewModelProvider.kt`
- Follow the existing pattern: `StateFlow` in ViewModel → `collectAsState()` in Screen → `LaunchedEffect` to trigger `load()`
- Match `@SerialName` annotations exactly to the DB column name

---

## 8. SRS Features Still Pending (Build These Next)

In priority order per `docs/SRS.md`:

1. **Grading Workflow** — marks enter as `Draft` → Teacher submits → Headteacher approves → status flips to `Published` → visible to Parent/Student. Add `status` column to `exam_results` if not present.
2. **Attendance entry UI for Teacher** — `TeacherDashboardScreen` shows class list, teacher marks Present/Absent/Late/Excused per student, saves to `attendance` table with today's date and duplicate prevention.
3. **Fee structure configuration** — Finance Officer can view/edit `fees` table entries per student type and term.
4. **Payment receipt generation** — Finance Officer records payment → auto-generates `RCP-YYYY-NNN` receipt number → saves to `payments` table.
5. **Announcement publishing** — Admin/Headteacher can compose and publish announcements to `announcements` table with audience targeting (`All`, `Parents`, `Students`, `Staff`).
6. **Boarding bed allocation** — Boarding Officer assigns student to dorm/room/bed in `boarding_allocations` table.
7. **Audit log** — every write operation logs to `audit_log` table: actor, action, entity, timestamp.
8. **Resend Email API** — volunteer application form on public screens sends email via Retrofit + Resend.

---

## 9. Gemini AI Integration

- Model: `gemini-2.0-flash`
- Key: `BuildConfig.GEMINI_API_KEY` (set in `local.properties` only — never commit this file)
- Used in: `TeacherDashboardScreen` (attendance insights), `FinanceDashboardScreen` (fee collection insights)
- Components: `GeminiInsightCard.kt` (one-shot summary), `GeminiChatBottomSheet.kt` (interactive Q&A)
- Repository: `GeminiRepository.kt` — wraps the Generative AI SDK calls

---

## 10. How to Add a New Feature (Step-by-Step)

1. **DB first**: Write SQL migration in `docs/supabase/migrations/` and apply it to Supabase
2. **Model**: Add/update `@Serializable` data class in `data/model/Models.kt` with exact `@SerialName` column names
3. **Repository**: Add method to the relevant repository interface and implement it in the `Impl` class
4. **AppContainer**: Wire the new method (no new repos needed if extending existing ones)
5. **ViewModel**: Add state field to the dashboard state data class; call the repository in `load()`
6. **Screen**: Add the new composable card/section to the dashboard screen, reading from `state`
7. **Test**: Run the app, log in as the relevant role, confirm live data appears

---

*This file is the single source of truth for all AI agents working on this codebase. When in doubt, read `docs/SRS.md` and this file before making any change.*
