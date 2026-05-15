# Handoff: Kalanikethan App Code Reorganization

## Project
Kotlin/Jetpack Compose Android tablet app for student attendance and payment tracking at an Indian dance school (Sunderland). Uses Supabase (PostgreSQL + Realtime + Auth) with MVVM architecture. Min SDK 33, Target/Compile SDK 36.

## What Has Been Done

### Phase 1: Constants + Immutable Models (COMPLETE)
- Created `util/Constants.kt` with `Tables` (table name strings) and `Timing` (debounce/delay constants)
- Created `ui/theme/Dimensions.kt` with `Dimens` object for hardcoded dp values
- Added named colors to `ui/theme/Color.kt` (`UnselectedChipBackground`, `BorderColor`, `InputBorderDefault`, `InputBorderError`, `SwitchUncheckedThumb`, `SwitchUncheckedTrack`)
- Moved Supabase URL to `BuildConfig.SUPABASE_URL` in `app/build.gradle.kts`
- Made data models immutable: `Class.kt`, `PaymentHistory.kt`, `Student.kt`, `Parent.kt`, `History.kt` — changed `var` fields to `val`
- Created `CLAUDE.md` at project root documenting architecture

### Phase 2: SessionManager (COMPLETE)
- Created `data/session/SessionManager.kt` — singleton holding auth state (`currentUser`, `authCompleted`, `uid`, `isManager`)
- Removed global mutable `var sessionPermissions` and `var authCompleted` from `User.kt`
- Updated `SupabaseClient.kt`, `AuthActivity.kt`, `AuthActivityViewmodel.kt`, `isManager.kt`, `TopAppBar.kt` to use `SessionManager`

### Phase 3: Split Repository (COMPLETE)
- Split monolith `data/repository/Repository.kt` (262 lines) into 7 domain-specific repositories:
  - `StudentRepository` — `getAllStudents()`, `addStudent()`
  - `AttendanceRepository` — `signInStudent()`, `signOutStudent()`
  - `ClassRepository` — `getAllClasses()`, `updateClass()`, `getStudentIdsForClass()`, `addStudentToClass()`, `removeStudentFromClass()`
  - `HistoryRepository` — `getAllHistories()`, `queryNullHistories()`
  - `EmployeeRepository` — `getAllEmployees()`
  - `FamilyRepository` — `getLastFamilyID()`, `addFamily()`, `getFamilyFromID()`, `addParent()`
  - `PaymentRepository` — `getPlanFromID()`, `addFirstFamilyPayment()`, `getUnpaidPayments()`, `confirmPayment()`, `getLatestPaymentFromFamilyID()`, `addPaymentToFamily()`, `getFamilyPaymentHistory()`, `addPaymentData()`
- Deleted `Repository.kt`

### Phase 4: Split God ViewModel (COMPLETE)
- Split `StudentsViewModel.kt` (448 lines) into 3 focused ViewModels:
  - `viewmodel/AttendanceViewModel.kt` — student list, search/filter, sign-in/out with debounce, realtime student channel
  - `viewmodel/ClassManagementViewModel.kt` — class list, student-class assignments, pending selections. Uses `bindAllStudents(StateFlow)` to receive student data from AttendanceViewModel
  - `viewmodel/HistoryViewModel.kt` — history records, employee list, realtime history channel, PDF export
- Created `viewmodel/AppViewModelFactory.kt` — single factory replacing 4 separate factories
- Updated `AddViewModel` constructor: `Repository` -> `StudentRepository + FamilyRepository + PaymentRepository`
- Updated `PaymentViewModel` constructor: `Repository` -> `PaymentRepository + FamilyRepository`; `_currentFamily` changed from `mutableStateOf` to `MutableStateFlow`
- Deleted `StudentsViewModel.kt`, `SignInViewModel.kt`, `DashBoardViewModel.kt`, all files in `Factories/`

### Phase 5: Package Restructure (COMPLETE)
- Extracted `Screen` sealed class from `MainActivity.kt` to `navigation/Screen.kt`
- Moved dashboard screens from `ui/screens/dashBoardViewModel/` to `ui/screens/dashboard/` (Dashboard, Classes, EditClass)
- Moved `WhoseIn.kt` to `ui/screens/whosein/`
- Moved `History.kt` to `ui/screens/history/`
- Updated all screen composable signatures to use the new split ViewModels

### Phase 6: Dead Code Cleanup (COMPLETE)
- Removed empty lifecycle methods from `MainActivity` (`onResume`, `onPause`, `onStop`, `onDestroy`, `onRestart`, `onStart`)
- Removed duplicate `FullscreenLoader()` call
- Fixed bug in `Add.kt` `checkError()`: checked `firstName.isBlank()` twice instead of `lastName.isBlank()` for second check
- Standardized ViewModel state to `MutableStateFlow`/`StateFlow` (replaced remaining `mutableStateOf` in ViewModels)
- Updated composable consumption sites to use `.collectAsState()` accordingly

### Post-Plan Fix: Classes Not Loading (COMPLETE)
- **Root cause**: `classVM.bindAllStudents(attendanceVM.allStudents)` was called in `MainActivity.onCreate()` before `setContent`, which forced `ClassManagementViewModel` creation before auth was ready. Its `init` block called `getAllClasses()` against Supabase without a valid session, so RLS returned empty results.
- **Fix**: Moved `bindAllStudents` call into a `LaunchedEffect(Unit)` inside `KalanikethanApp` composable, which only renders after `SessionStatus.Authenticated`.
- Also added missing `Payments` and `PaymentHistory` routes to the NavHost.

### Post-Plan Fix: Realtime Refactor (COMPLETE)
- Deleted `data/models/Channel.kt` — over-engineered wrapper with `shareIn(WhileSubscribed(5s))` that could miss events
- Rewrote `data/remote/ChannelManager.kt` — lazy channel creation, no artificial delays, real `disconnectAll()`, removed unused payment channel
- Rewrote `AttendanceViewModel` — channel subscription moved to private `subscribeToStudentChanges()` called once from `init`. Removed public `initialiseStudentsChannel()`. Simplified `updateSearchQuery()` by removing unnecessary `withContext(Dispatchers.Unconfined)`.
- Rewrote `HistoryViewModel` — `subscribeToHistoryChanges()` now called in `init`, so history updates arrive in realtime (was previously defined but never called — History screen had zero realtime)
- Cleaned up `SignIn.kt` and `WhoseIn.kt` — removed `LaunchedEffect { initialiseStudentsChannel() }` calls and no-op `ChannelManager.unsubscribeFromAllChannels()` from edit handlers
- Removed `CHANNEL_SUBSCRIBE_DELAY_MS` from `Constants.kt`

## Current State
- **Build**: `./gradlew assembleDebug` passes with BUILD SUCCESSFUL
- **Nothing is committed** — all changes are unstaged working tree modifications
- All 6 original plan phases + 2 post-plan fixes are complete

## What Was NOT Done Yet — UI Compose Fixes

The user asked to audit all UI screens and composables for Compose correctness and reactivity, without changing the visual appearance. I identified 9 categories of issues but was interrupted before implementing any fixes. **None of these fixes have been applied yet.** Here is exactly what needs to be done:

### Bug 1: SimpleDecoratedTextField internal state shadowing (HIGH PRIORITY)
**File**: `ui/components/SimpleTextBox.kt`
**Line 113**: `val text = remember { mutableStateOf(text) }` 

This shadows the `text` parameter with internal state that is initialized once and never updated when the parent changes. This means:
- When `removeSearchQuery()` clears the search, the text field still shows the old query
- Any external state change to `text` is ignored

**Fix**: Remove the internal state. Make it a fully controlled component — use the `text` parameter directly as `BasicTextField`'s `value`. Update the clear button and date picker to just call `onValueChange()` instead of mutating internal state.

Specifically:
- Delete line 113 (`val text = remember { mutableStateOf(text) }`)
- Change all `text.value` references to just `text` (the parameter)
- In `onValueChange` of `BasicTextField`: remove `text.value = it`, just call the parent's `onValueChange(it)` (keeping the `floatsOnly` validation)
- Clear button: change `text.value = ""` to just `onValueChange("")`
- Date picker callback: change `text.value = date.format(formatter)` + `onValueChange(text.value)` to just `onValueChange(formatted)`

### Bug 2: SimpleDecoratedTextField2 internal state shadowing (HIGH PRIORITY)
**File**: `ui/components/SimpleTextBox2.kt`
**Line 128**: Same exact issue as Bug 1.

**Fix**: Identical approach. Also fix the time picker callback:
- Change `text.value = "${timePickerState.hour}:${timePickerState.minute}"` + `onValueChange(text.value)` to just `onValueChange("${timePickerState.hour}:${timePickerState.minute}")`

### Bug 3: TopAppBar reads StateFlow.value directly (MEDIUM)
**File**: `ui/components/TopAppBar.kt`
**Line 112**: `SessionManager.currentUser.value.first_name`

This reads the StateFlow's `.value` directly inside a composable. It will show the correct value on first render but won't trigger recomposition when the user changes (e.g., after re-auth).

**Fix**: Add at the top of the `TopAppBar` composable:
```kotlin
val currentUser by SessionManager.currentUser.collectAsState()
```
Then change line 112 to `currentUser.first_name`. Add the necessary import for `collectAsState`.

### Bug 4: WhoseIn creates new flow on every recomposition (MEDIUM)
**File**: `ui/screens/whosein/WhoseIn.kt`
**Lines 37-40**:
```kotlin
val students = attendanceViewModel.displayedStudents
    .map { studentList -> studentList.filter { student -> student.signedIn } }
    .collectAsState(emptyList())
```

`.map {}` creates a new `Flow` object on every recomposition because the lambda is recreated. The `collectAsState` then restarts collection on the new flow.

**Fix**: Wrap in `remember` to stabilize:
```kotlin
val students by remember {
    attendanceViewModel.displayedStudents
        .map { it.filter { student -> student.signedIn } }
}.collectAsState(emptyList())
```

### Bug 5: BottomBar wrong tab title (LOW)
**File**: `ui/components/BottomBar.kt`
**Line 88**: `title = "com/lra/kalanikethan/ui/screens/Payments"`

This shows a package path as the tab title.

**Fix**: Change to `title = "Payments"`

### Bug 6: ClassStudent.kt stale import (LOW)
**File**: `ui/components/ClassStudent.kt`
**Line 31**: `import io.github.jan.supabase.realtime.Column`

This unused import shadows Compose's `Column`. It happens to not cause a compile error because the preview function uses the Compose `Column` from the wildcard, but it's confusing and fragile.

**Fix**: Delete line 31.

### Anti-pattern 7: Sorting inside items() + missing keys (MEDIUM)
**Files**: `ui/screens/dashboard/Classes.kt` line 91, `ui/screens/dashboard/EditClass.kt` lines 178 and 211

`items(students.sortedBy { it.firstName })` creates a new sorted list on every recomposition. Also missing `key` parameters which hurts list performance and animations.

**Fix for Classes.kt**:
```kotlin
val sortedStudents = remember(students) { students.sortedBy { it.firstName } }
// ...
items(sortedStudents, key = { it.studentId ?: 0 }) { student -> ... }
```

**Fix for EditClass.kt** (line 178, the left panel students list):
```kotlin
val sortedClassStudents = remember(studentsInClass) { studentsInClass.sortedBy { it.firstName } }
// ...
items(sortedClassStudents, key = { it.studentId ?: 0 }) { student -> ... }
```

**Fix for EditClass.kt** (line 211, the right panel all-students list):
```kotlin
items(allStudents.value, key = { it.studentId ?: 0 }) { student -> ... }
```

### Anti-pattern 8: collectAsState(emptyList()) on StateFlows (LOW)
**Files**: `Dashboard.kt` line 46, `History.kt` lines 61-64, `Classes.kt` line 40

`StateFlow` already has an initial value. Using `collectAsState(emptyList())` calls the `Flow` overload instead of the `StateFlow` overload. Functionally equivalent but not idiomatic.

**Fix**: Change all `collectAsState(emptyList())` on StateFlow to `collectAsState()`. Examples:
- `Dashboard.kt`: `classViewModel.allClasses.collectAsState()` (returns the same type)
- `History.kt`: `historyViewModel.histories.collectAsState()`, `attendanceViewModel.allStudents.collectAsState()`, `historyViewModel.employees.collectAsState()`, `classViewModel.allClasses.collectAsState()`

### Anti-pattern 9: PaymentHistoryData mutable vars (LOW)
**File**: `ui/screens/Payments/PaymentHistory.kt` lines 33-38
```kotlin
data class PaymentHistoryData (
    var history : List<PaymentHistory> = emptyList(),
    var familyName : String = "",
    var familyID : String = "",
    var amount : String = "",
)
```

**Fix**: Change all `var` to `val`. The data class is never mutated in place — it's always created with all fields set via `PaymentHistoryData(history = ..., familyName = ..., ...)`.

### Anti-pattern 10: Cleanup (LOW)
Various files need minor cleanup. These don't affect behavior:

- **StudentInfoCard.kt**: Remove unused imports (`android.icu.text.IDNA` line 3, `java.time.LocalDate` line 39). Remove commented-out "Sign Absent" button block (lines 201-209). Fix broken preview (`birthdate = TODO()` on line 236 — change to `birthdate = kotlinx.datetime.LocalDate(2000, 1, 1)`).
- **ClassBox.kt**: Remove unnecessary `@RequiresApi(Build.VERSION_CODES.O)` (min SDK is 33, O is 26). Remove `@OptIn(ExperimentalFoundationApi::class)` (nothing experimental is used). Remove dead code: `val darkTheme = false`, `val istime = false`, the unused `startTime`/`endTime` val assignments (lines 53-58). Replace `if (darkTheme)` ternaries with the light-theme value directly.
- **SimpleTextBox.kt**: Remove unused imports: `SimpleDateFormat`, `Date`, `Locale`, `Instant`, `LocalDate`, `CoroutineScope`.
- **SimpleTextBox2.kt**: Remove unused imports: `SimpleDateFormat`, `Date`, `Locale`, `Instant`, `LocalDate`, `CoroutineScope`, `Calendar`, `ModifierLocalBeyondBoundsLayout`, `TimePickerDialog` (Android), `Button` (Material3 — shadows our custom Button), `DatePicker`, `DatePickerDefaults`.
- **KalanikethanAppDrawer.kt**: Uncomment `Screen.Payments` in the drawer items list (line 113) if Payments screen is meant to be accessible, or remove the commented line.

## File Inventory (Current State After All Changes)

### New files (untracked):
```
CLAUDE.md
app/src/main/java/com/lra/kalanikethan/data/repository/AttendanceRepository.kt
app/src/main/java/com/lra/kalanikethan/data/repository/ClassRepository.kt
app/src/main/java/com/lra/kalanikethan/data/repository/EmployeeRepository.kt
app/src/main/java/com/lra/kalanikethan/data/repository/FamilyRepository.kt
app/src/main/java/com/lra/kalanikethan/data/repository/HistoryRepository.kt
app/src/main/java/com/lra/kalanikethan/data/repository/PaymentRepository.kt
app/src/main/java/com/lra/kalanikethan/data/repository/StudentRepository.kt
app/src/main/java/com/lra/kalanikethan/data/session/SessionManager.kt
app/src/main/java/com/lra/kalanikethan/navigation/Screen.kt
app/src/main/java/com/lra/kalanikethan/ui/screens/dashboard/Classes.kt
app/src/main/java/com/lra/kalanikethan/ui/screens/dashboard/Dashboard.kt
app/src/main/java/com/lra/kalanikethan/ui/screens/dashboard/EditClass.kt
app/src/main/java/com/lra/kalanikethan/ui/screens/history/History.kt
app/src/main/java/com/lra/kalanikethan/ui/screens/whosein/WhoseIn.kt
app/src/main/java/com/lra/kalanikethan/ui/theme/Dimensions.kt
app/src/main/java/com/lra/kalanikethan/util/Constants.kt
app/src/main/java/com/lra/kalanikethan/viewmodel/AppViewModelFactory.kt
app/src/main/java/com/lra/kalanikethan/viewmodel/AttendanceViewModel.kt
app/src/main/java/com/lra/kalanikethan/viewmodel/ClassManagementViewModel.kt
app/src/main/java/com/lra/kalanikethan/viewmodel/HistoryViewModel.kt
```

### Deleted files:
```
app/src/main/java/com/lra/kalanikethan/Factories/AddViewModelFactory.kt
app/src/main/java/com/lra/kalanikethan/Factories/DashBoardViewModelFactory.kt
app/src/main/java/com/lra/kalanikethan/Factories/SignInViewModelFactory.kt
app/src/main/java/com/lra/kalanikethan/Factories/StudentsViewModelFactory.kt
app/src/main/java/com/lra/kalanikethan/StudentsViewModel.kt
app/src/main/java/com/lra/kalanikethan/data/models/Channel.kt
app/src/main/java/com/lra/kalanikethan/data/repository/Repository.kt
app/src/main/java/com/lra/kalanikethan/ui/screens/History.kt (moved to history/)
app/src/main/java/com/lra/kalanikethan/ui/screens/WhoseIn.kt (moved to whosein/)
app/src/main/java/com/lra/kalanikethan/ui/screens/dashBoardViewModel/Classes.kt (moved to dashboard/)
app/src/main/java/com/lra/kalanikethan/ui/screens/dashBoardViewModel/DashBoardViewModel.kt
app/src/main/java/com/lra/kalanikethan/ui/screens/dashBoardViewModel/Dashboard.kt (moved to dashboard/)
app/src/main/java/com/lra/kalanikethan/ui/screens/dashBoardViewModel/EditClass.kt (moved to dashboard/)
app/src/main/java/com/lra/kalanikethan/ui/screens/signIn/SignInViewModel.kt
```

## Key Architecture Decisions

1. **Cross-ViewModel data sharing**: `ClassManagementViewModel` needs student data from `AttendanceViewModel` for the `selectedStudentsForActiveClass` flow. Solved with `bindAllStudents(StateFlow)` — called in `KalanikethanApp`'s `LaunchedEffect(Unit)`, after auth is confirmed.

2. **Realtime architecture**: Each ViewModel manages its own channel subscription in `init`. `ChannelManager` is a thin singleton that lazily creates `RealtimeChannel` instances. VMs create `postgresChangeFlow` and call `channel.subscribe()` directly. Composables don't interact with channels at all.

3. **History duplication**: `AttendanceViewModel` keeps its own `_histories` (loaded via REST, refreshed after each sign-in/out) for sign-out lookup. `HistoryViewModel` keeps its own copy updated via realtime channel for display. These are intentionally separate.

4. **ViewModel creation timing**: All VMs are created via `by viewModels { factory }` (lazy). They're first accessed when `KalanikethanApp(attendanceVM, classVM, ...)` is called inside the `SessionStatus.Authenticated` branch, ensuring auth is ready when `init` blocks run.

## Constraint
**Do NOT change the visual appearance of the app.** The user explicitly said "I like the way the UI looks so don't change the way it looks." All fixes should be structural/reactive only. Minor improvements to professionalism are OK (e.g., fixing the Payments tab title), but layout, colors, spacing, and component structure should remain unchanged.
