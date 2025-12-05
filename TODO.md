# Project To-Do List

## Phase 1: Setup & Configuration
- [x] Add Supabase and Ktor dependencies to `libs.versions.toml` and `build.gradle.kts`.
- [x] Add Internet permission (`android.permission.INTERNET`) to `AndroidManifest.xml`.
- [x] Configure Supabase URL and API Key (Secrets management).
- [x] Initialize Supabase local development environment using `npx supabase init`.

## Phase 2: Data Layer
- [x] Create database migrations for `cars` table using `npx supabase migration new`.
- [ ] Apply migrations to remote Supabase project using `npx supabase db push`.
- [x] Create `Car` data class (Model) representing the database table.
- [x] Create `SupabaseClient` singleton/module for initialization.
- [x] Create a Repository to handle data fetching (fetch cars, search cars).

## Phase 3: UI Implementation
- [ ] Create `AuthScreen` (Login/Signup).
- [ ] Create `CarListScreen` to display lost and found cars.
- [ ] Create `SearchScreen` to query the database.
- [ ] Create `AddCarScreen` (optional) to report lost/found cars.
- [ ] Implement Navigation between screens.

## Phase 4: Testing & Refinement
- [ ] Test database connection.
- [ ] Verify search functionality.
- [ ] Polish UI with Material3 components.
