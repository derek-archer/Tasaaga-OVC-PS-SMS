# Junior Developer Guide: 04. Developer Onboarding & Extension Handbook

Welcome to the team! This handbook will get you up and running on your local machine and guide you through adding your first feature to **TSMS**.

---

## ⚡ 1. Prerequisites & Environment Setup

1. **Android Studio**: Android Studio Ladybug or newer with JDK 17+.
2. **Android SDK**: Android 15 (API 35) or Android 16 (API 37).
3. **Git**: Installed and configured.

---

## 🚀 2. Getting Started (Build & Run)

### Step 1: Clone the Repository & Open Project
Open the project root directory in Android Studio.

### Step 2: Run Unit Tests
Run tests to verify that your environment is configured correctly:
```bash
./gradlew app:testDebugUnitTest
```

### Step 3: Build Debug APK
Build the Debug APK:
```bash
./gradlew app:assembleDebug
```
The APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`.

---

## 🛠️ 3. Tutorial: How to Add a New Feature to TSMS

Let's walk through adding a new feature (e.g. **Library Book Search**).

### Step 1: Define the Data Model (`Models.kt`)
Add the Kotlin model with `@Serializable` and `@SerialName`:

```kotlin
@Serializable
data class LibraryBook(
    val id: Int? = null,
    val title: String,
    val author: String,
    val category: String,
    @SerialName("is_available")
    val isAvailable: Boolean = true
)
```

### Step 2: Create the Repository (`LibraryRepository.kt`)
Create the repository interface and implementation with local fallback:

```kotlin
interface LibraryRepository {
    fun getBooks(): Flow<List<LibraryBook>>
}

class LibraryRepositoryImpl(private val supabaseClient: SupabaseClient) : LibraryRepository {
    private val defaultBooks = listOf(
        LibraryBook(1, "Primary Science P.5", "NCDC Uganda", "Science"),
        LibraryBook(2, "English Reader P.6", "Tasaaga Press", "English")
    )

    override fun getBooks(): Flow<List<LibraryBook>> = flow {
        val remote = try {
            supabaseClient.postgrest.from("library_books").select().decodeList<LibraryBook>()
        } catch (e: Exception) {
            emptyList()
        }
        emit(if (remote.isNotEmpty()) remote else defaultBooks)
    }
}
```

### Step 3: Inject in `AppContainer.kt`
Add the repository to `AppContainer`:

```kotlin
interface AppContainer {
    val libraryRepository: LibraryRepository
    // ...
}

class AppContainerImpl : AppContainer {
    override val libraryRepository: LibraryRepository by lazy { 
        LibraryRepositoryImpl(supabaseClient) 
    }
}
```

### Step 4: Create the ViewModel & Composable Screen
1. Create `LibraryViewModel` exposing `val books = libraryRepository.getBooks().stateIn(...)`.
2. Create `LibraryScreen.kt` observing `viewModel.books.collectAsState()`.
3. Add `LibraryRoute` to `TasaagaNavigation.kt` and register in `TasaagaApp.kt` `entryProvider`.

---

## ❓ 4. Need Help?

- **SRS Document**: Read [`docs/SRS.md`](file:///C:/Users/derri/AndroidStudioProjects/TasaagaOVCPS/docs/SRS.md) for full functional & business specifications.
- **Database Schema**: Check [`docs/supabase/schema.sql`](file:///C:/Users/derri/AndroidStudioProjects/TasaagaOVCPS/docs/supabase/schema.sql) and [`docs/supabase/seed.sql`](file:///C:/Users/derri/AndroidStudioProjects/TasaagaOVCPS/docs/supabase/seed.sql).
- **Architecture Overview**: Review [`docs/junior_dev_guides/01_ARCHITECTURE_OVERVIEW.md`](file:///C:/Users/derri/AndroidStudioProjects/TasaagaOVCPS/docs/junior_dev_guides/01_ARCHITECTURE_OVERVIEW.md).
