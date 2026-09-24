# Junior Developer Guide: 01. Architecture Overview & Design Patterns

Welcome to the **Tasaaga School Management System (TSMS)** Android Application codebase! This guide explains the software architecture, design patterns, and tech stack choices used across the app.

---

## 🏛️ 1. High-Level System Architecture

TSMS follows **Clean Architecture** principles combined with **Unidirectional Data Flow (UDF)** and modern **Android Jetpack** standards.

```
┌─────────────────────────────────────────────────────────┐
│                      UI LAYER                           │
│  Jetpack Compose Screens & Material 3 Brand System     │
│  (HomeScreen, LoginScreen, Dashboards, SupportScreen)   │
└───────────────────────────▲─────────────────────────────┘
                            │ (StateFlow / UI State)
┌───────────────────────────┴─────────────────────────────┐
│                    VIEWMODEL LAYER                      │
│  Coroutines Flow, State Management, Input Validation    │
│  (LoginViewModel, NewsViewModel, VolunteerViewModel)    │
└───────────────────────────▲─────────────────────────────┘
                            │ (Repositories Interface)
┌───────────────────────────┴─────────────────────────────┐
│                   REPOSITORY LAYER                      │
│  AppContainer Dependency Injection Singleton           │
│  UserRepository, NewsRepository, VolunteerRepository   │
└─────────────────────▲───────────▲───────────────────────┘
                      │           │
       (Postgrest REST)           (Local Fallback Defaults)
                      │           │
       ┌──────────────┴──┐     ┌──┴──────────────┐
       │ Supabase Remote │     │  Local Default  │
       │ PostgreSQL DB   │     │  Kotlin Objects │
       └─────────────────┘     └─────────────────┘
```

---

## 🛠️ 2. Technology Stack & Key Libraries

| Component | Library / Framework | Version / Details | Purpose |
| :--- | :--- | :--- | :--- |
| **Language** | Kotlin | `2.0.21` | Modern, type-safe concise programming language |
| **UI Framework** | Jetpack Compose | Material 3 (`BOM 2024.09.00`) | Declarative, reactive UI rendering |
| **Navigation** | Navigation 3 | `androidx.navigation3:1.0.0-alpha01` | Type-safe stateful route backstack navigation |
| **Backend & Auth** | Supabase Kotlin SDK | `io.github.jan-tennert.supabase` v3.0.3 | Postgrest REST API & Supabase Auth client |
| **Serialization** | Kotlinx Serialization | `@Serializable` / `@SerialName` | JSON encoding/decoding for Postgrest payloads |
| **HTTP & Emails** | Retrofit 2 + Moshi | `2.9.0` + Moshi Kotlin Codegen | Resend Email API integration for volunteer forms |
| **Build & KSP** | Gradle + KSP | Kotlin Symbol Processing | Fast code generation for Room & Moshi |

---

## 🔁 3. Unidirectional Data Flow (UDF) Pattern

In TSMS, data flows in a single direction:

1. **User Action**: User taps a button on a Compose Screen (e.g. *Login*, *Apply*, *Donate*, *Take Attendance*).
2. **ViewModel Event**: The Screen calls a function on the `ViewModel`.
3. **Repository Execution**: The ViewModel launches a Coroutine in `viewModelScope` and calls the `Repository`.
4. **State Update**: The Repository queries Supabase (falling back to local default data if offline) and emits new data via `StateFlow` or `Flow`.
5. **UI Render**: The Composable Screen observes the `StateFlow` via `collectAsState()` and automatically re-renders the UI with zero manual DOM manipulation.

---

## 🧩 4. Repository Pattern & Resilient Offline Fallback

Every data entity (`NewsItem`, `VolunteerOpportunity`, `SchoolInfo`, `Profile`) has a Repository interface:

```kotlin
// 1. Repository Interface Definition
interface NewsRepository {
    fun getNewsItems(): Flow<List<NewsItem>>
}

// 2. Implementation with Live Query & Graceful Local Fallback
class NewsRepositoryImpl(private val supabaseClient: SupabaseClient) : NewsRepository {
    
    override fun getNewsItems(): Flow<List<NewsItem>> = flow {
        val remoteNews = try {
            supabaseClient.postgrest.from("news_items").select().decodeList<NewsItem>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
        
        // Emit remote data if available; otherwise emit offline default fallback
        if (remoteNews.isNotEmpty()) {
            emit(remoteNews)
        } else {
            emit(defaultNews)
        }
    }
}
```

---

## 📂 5. Project Package Structure

```
com.example.tasaagaovcps/
├── data/
│   ├── model/           # Kotlin Data Models (@Serializable & @SerialName)
│   ├── repository/      # Repository Interfaces & Implementations
│   └── AppContainer.kt  # Dependency Injection Singleton
├── ui/
│   ├── components/      # Reusable Composables (TasaagaLogo, Cards, Badges)
│   ├── navigation/      # Navigation 3 Routes (TasaagaRoute)
│   ├── screens/         # Public & Role Portal Screens (HomeScreen, Dashboards)
│   ├── theme/           # Color palette, Type, Material 3 Theme tokens
│   └── viewmodel/       # App ViewModels & AppViewModelProvider Factory
```
