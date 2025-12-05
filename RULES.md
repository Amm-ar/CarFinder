# Project Rules & Guidelines

## Tech Stack
- **Language**: Kotlin (latest stable).
- **UI Framework**: Jetpack Compose.
- **Design System**: Material Design 3.
- **Backend**: Supabase (PostgreSQL).
- **Networking**: Ktor (or Supabase Kt client).
- **Architecture**: MVVM (Model-View-ViewModel).
- **Tools**: Supabase CLI (`npx supabase`) for database management.

## Coding Standards
1. **Kotlin First**: Use idiomatic Kotlin features (Coroutines, Flow, Extension functions).
2. **Compose Best Practices**:
    - Hoist state when possible.
    - Use `Modifier` as the first optional parameter.
    - Create small, reusable composables.
3. **State Management**: Use `ViewModel` with `StateFlow` or `Compose State`.
4. **Dependency Injection**: Use manual DI or Hilt (if introduced later).
5. **Async Operations**: All database/network calls must be performed on IO dispatchers using Coroutines.

## Project Structure
- `ui/`: Contains all Composable screens and themes.
- `data/`: Contains Models, Repositories, and API clients.
- `viewmodel/`: Contains ViewModels managing UI state.
- `supabase/`: Contains migrations and configuration files (managed by CLI).

## Supabase Workflow
- Use `npx supabase` commands for all database schema changes.
- Create migrations with `npx supabase migration new <name>`.
- Apply changes with `npx supabase db push` or `npx supabase db reset` for local dev.
