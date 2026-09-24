# Junior Developer Guide: 02. Supabase PostgreSQL & Postgrest API Guide

This guide explains how the **Supabase PostgreSQL** backend, database tables, Row-Level Security (RLS), and API endpoints are structured and integrated into the TSMS Android application.

---

## 🌐 1. Supabase Connection Credentials

The app communicates with Supabase using the Postgrest REST API:

- **Supabase URL**: `https://dibxccfjbntfnzhqpcuv.supabase.co`
- **Publishable Anon Key**: `sb_publishable_WTcoJ9DnRot425mTmrQFfQ_UeUEm_0h`
- **Configuration File**: [`AppContainer.kt`](file:///C:/Users/derri/AndroidStudioProjects/TasaagaOVCPS/app/src/main/java/com/example/tasaagaovcps/data/AppContainer.kt)

```kotlin
class AppContainerImpl : AppContainer {
    override val supabaseClient: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://dibxccfjbntfnzhqpcuv.supabase.co",
        supabaseKey = "sb_publishable_WTcoJ9DnRot425mTmrQFfQ_UeUEm_0h"
    ) {
        install(Auth) {
            sessionManager = MemorySessionManager()
            codeVerifierCache = MemoryCodeVerifierCache()
        }
        install(Postgrest)
    }
}
```

---

## 🗄️ 2. The 22 Core Database Tables Overview

The backend consists of 22 PostgreSQL tables created in [`docs/supabase/schema.sql`](file:///C:/Users/derri/AndroidStudioProjects/TasaagaOVCPS/docs/supabase/schema.sql):

| # | Table Name | Key Columns | Purpose |
| :-: | :--- | :--- | :--- |
| **1** | `profiles` | `id` (UUID), `role`, `email`, `name`, `phone` | Role-based user accounts & permissions |
| **2** | `students` | `id`, `adm_no`, `fname`, `lname`, `gender`, `student_type`, `class_id` | Student master register (`TAS-2026-xxx`) |
| **3** | `parents` | `id`, `name`, `phone`, `email`, `children` | Guardian contacts & linked children |
| **4** | `classes` | `id`, `name`, `level`, `stream`, `teacher_name` | Primary class streams (`P.1A` to `P.7A`) |
| **5** | `subjects` | `id`, `code`, `name`, `level`, `is_core` | Core subjects (MTH, ENG, SCI, SST, RE) |
| **6** | `attendance` | `id`, `student_id`, `status`, `recorded_by`, `date` | Daily attendance (`Present`, `Absent`, `Late`) |
| **7** | `fee_structures` | `id`, `category`, `student_type`, `amount`, `term` | Fee rates for Day vs. Boarding |
| **8** | `payments` | `id`, `receipt_no`, `student_id`, `amount`, `payment_method` | Fee payments & receipts (`RCP-2026-xxxx`) |
| **9** | `expenses` | `id`, `date`, `category`, `description`, `amount`, `status` | Operational expenses & approvals |
| **10** | `boarding_dorms` | `id`, `name`, `gender`, `capacity`, `occupied` | Dormitory capacity tracking (Dorm A - D) |
| **11** | `boarding_allocations` | `id`, `student_id`, `dorm_name`, `room`, `bed` | Dorm room and bed allocations |
| **12** | `boarding_welfare` | `id`, `student_name`, `incident_type`, `description` | Health and welfare incident logs |
| **13** | `staff` | `id`, `name`, `role`, `department`, `phone`, `email` | Staff directory and roles |
| **14** | `inventory_assets` | `id`, `asset_code`, `name`, `condition`, `location` | Physical school assets and equipment |
| **15** | `inventory_stock` | `id`, `item_name`, `quantity`, `min_level`, `unit` | Supplies stock & low-stock alerts |
| **16** | `inventory_movements` | `id`, `date`, `item_name`, `movement_type`, `quantity` | Stock receipts and issues audit |
| **17** | `announcements` | `id`, `title`, `body`, `audience`, `published_by` | School notices (`All`, `Parents`, `Staff`) |
| **18** | `audit_logs` | `id`, `timestamp`, `actor`, `action`, `entity`, `details` | System audit trail |
| **19** | `news_items` | `id`, `title`, `content`, `category`, `date`, `image_url` | Community & Musawo clinic news |
| **20** | `volunteer_opportunities` | `id`, `title`, `description`, `requirements`, `duration` | Placement opportunities for volunteers |
| **21** | `school_info` | `id`, `mission`, `motto`, `programs` | Core school identity and programs |
| **22** | `donation_info` | `id`, `title`, `description`, `donation_methods` | OVC sponsorship details and channels |

---

## 🔄 3. Postgrest Mapping with `@Serializable` and `@SerialName`

Kotlin models use `kotlinx.serialization` to map Postgrest JSON keys (snake_case) to Kotlin camelCase properties:

```kotlin
@Serializable
data class NewsItem(
    val id: String,
    val title: String,
    val content: String,
    val category: NewsCategory,
    val date: String,
    @SerialName("image_url") // Maps Postgres column image_url to Kotlin imageUrl
    val imageUrl: String? = null
)
```

---

## ⚡ 4. Direct DDL Execution via `exec_sql` RPC

To run DDL queries (`CREATE TABLE`, `ALTER TABLE`) directly via Postgrest API, TSMS uses a custom PostgreSQL function:

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

This function allows executing migration scripts programmatically using the Service Role Key!

---

## 🔑 5. Demo Accounts Matrix for Testing

All demo accounts use password **`Password123!`**:

- 🔧 **Admin**: `admin@tasaagaschool.org` (or `derekmukasa@gmail.com`)
- 👔 **Headteacher**: `headteacher@tasaagaschool.org`
- 👩‍🏫 **Teacher**: `teacher@tasaagaschool.org`
- 💳 **Finance**: `finance@tasaagaschool.org`
- 👨‍👩‍👧 **Parent**: `parent@tasaagaschool.org`
- 🏘️ **Boarding**: `boarding@tasaagaschool.org`
- 👦 **Student**: `student@tasaagaschool.org`
