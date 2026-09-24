# Junior Developer Guide: 03. UI, Navigation 3 & Design Tokens

This guide covers the **Jetpack Compose UI**, **Navigation 3**, and the **Material 3 Tasaaga Brand Design System**.

---

## 🎨 1. Tasaaga Brand System & Design Tokens

TSMS uses an authentic Ugandan school color system defined in [`Color.kt`](file:///C:/Users/derri/AndroidStudioProjects/TasaagaOVCPS/app/src/main/java/com/example/tasaagaovcps/ui/theme/Color.kt):

| Token | Color Code | Visual Role | Sample Usage |
| :--- | :--- | :--- | :--- |
| **Brand Red** | `Color(0xFFB71C1C)` | Primary action, header banners, CTA buttons | Header banners, primary buttons, alert tags |
| **Brand Gold / Yellow** | `Color(0xFFFDD835)` | Accent, portal login button, quote borders | Quote card accents, portal login CTA |
| **Brand Green** | `Color(0xFF1B5E20)` | Section titles, success states, positive progress | Section headers, "Present" attendance badge |
| **Brand Dark Brown** | `Color(0xFF3E2723)` | Footer background, deep structural contrast | Public page footers, card dividers |
| **Surface Light** | `Color(0xFFF9F9F9)` | Background neutral tint | Page backgrounds, card containers |

---

## 🧭 2. Navigation 3 Architecture (`androidx.navigation3`)

TSMS implements type-safe routes extending `TasaagaRoute`:

```kotlin
// 1. Sealed Interface Route Hierarchy
sealed interface TasaagaRoute {
    val route: String
    val label: String
    val icon: ImageVector
}

// 2. Top-Level Public Routes
object HomeRoute : TasaagaRoute { ... }
object SupportRoute : TasaagaRoute { ... }
object VolunteerRoute : TasaagaRoute { ... }
object CommunityRoute : TasaagaRoute { ... }
object MoreRoute : TasaagaRoute { ... }

// 3. Sub-Routes (Role Portals)
object LoginRoute : TasaagaRoute { ... }
object AdminDashboardRoute : TasaagaRoute { ... }
object TeacherDashboardRoute : TasaagaRoute { ... }
```

### TopLevelBackStack Helper:
[`TasaagaApp.kt`](file:///C:/Users/derri/AndroidStudioProjects/TasaagaOVCPS/app/src/main/java/com/example/tasaagaovcps/ui/TasaagaApp.kt) manages a top-level tab backstack:

```kotlin
val topLevelBackStack = remember { TopLevelBackStack<TasaagaRoute>(HomeRoute) }

NavDisplay(
    backStack = topLevelBackStack.backStack,
    onBack = { topLevelBackStack.removeLast() },
    entryProvider = entryProvider {
        entry<HomeRoute> { HomeScreen(...) }
        entry<LoginRoute> { LoginScreen(...) }
        entry<TeacherDashboardRoute> { TeacherDashboardScreen(...) }
    }
)
```

---

## 📱 3. Pre-Login Public Screens Design Pattern

The public pre-login experience ([`HomeScreen.kt`](file:///C:/Users/derri/AndroidStudioProjects/TasaagaOVCPS/app/src/main/java/com/example/tasaagaovcps/ui/screens/HomeScreen.kt)) uses a 9-part structured composition:

1. **Header Banner**: School logo, motto *"Rising To Succeed"*, and yellow **Portal Login** CTA.
2. **Hero Banner**: *"A brighter future starts here."* with **Explore Our School** & **Admissions Enquiry** buttons.
3. **Welcome & Quote Card**: Gold vertical accent bar + quote *"Every child deserves a chance to learn..."*.
4. **Our Programmes**: 4-card grid (Primary Education, Day & Boarding, Meals, Life Skills).
5. **Admissions Journey**: 4-Step timeline (`1 Enquire` → `2 Visit` → `3 Apply` → `4 Review`).
6. **Community Support**: 3 Support action cards + *"Discuss how to help"* CTA.
7. **Database-Driven News**: Live cards loaded from Supabase `news_items` via `NewsViewModel`.
8. **FAQ Accordion & Live Inquiry Form**: Expandable questions + *"Let's talk"* inquiry form with regex validation.
9. **Brand Footer**: Location info (*Sitabaale, Uganda \| Day & Boarding*) + Quick Links.

---

## 📝 4. Form Validation & Pattern Matching

Interactive forms (`VolunteerScreen.kt`, `ContactScreen.kt`, `SupportScreen.kt`) implement live pattern validation:

```kotlin
val isNameValid = name.isNotBlank()
val isEmailValid = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
val isMessageValid = message.isNotBlank()

OutlinedTextField(
    value = email,
    onValueChange = { email = it },
    label = { Text("Email Address *") },
    isError = hasAttemptedSubmit && !isEmailValid,
    supportingText = {
        if (hasAttemptedSubmit && !isEmailValid) {
            Text("Invalid email format", color = MaterialTheme.colorScheme.error)
        }
    }
)
```
