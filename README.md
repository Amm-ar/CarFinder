# 🚗 Car Finder - Lost Vehicle Recovery App

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-blue.svg)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-13+-green.svg)](https://developer.android.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Supabase](https://img.shields.io/badge/Backend-Supabase-purple.svg)](https://supabase.com)

Car Finder is a modern Android application designed to help users locate lost vehicles and manage lost/found car listings through a cloud-based backend using Supabase.

## ✨ Features

### 🔍 Search & Discovery
- **Smart Vehicle Search**: Search by license plate, VIN, make, model, color, or location
- **Geolocation Filtering**: Search within specific radius using GPS coordinates
- **Real-time Results**: Live updates with Supabase real-time subscriptions

### 📋 Vehicle Management
- **Lost Vehicle Registry**: Report missing vehicles with photos and detailed descriptions
- **Found Vehicle Tracking**: Register found vehicles and update status
- **Vehicle Photos**: Upload and display multiple images per vehicle
- **Status Updates**: Track vehicle status (lost, found, claimed, resolved)

### 👤 User Experience
- **User Authentication**: Secure sign-up/login with Supabase Auth
- **Favorites System**: Save frequently searched vehicles
- **Search History**: Track previous searches and results
- **Push Notifications**: Alerts for vehicle status changes

### 🔄 Data Management
- **Supabase Integration**: Full CRUD operations with real-time sync
- **Offline Capability**: Local caching with Room database
- **Image Storage**: Secure file uploads to Supabase Storage
- **Data Validation**: Input validation and error handling

### 🎨 Modern UI/UX
- **Jetpack Compose**: 100% declarative UI with Material Design 3
- **Dark/Light Theme**: Automatic theme switching
- **Accessibility**: Full screen reader and navigation support
- **Responsive Design**: Optimized for various screen sizes

## 🛠 Technical Stack

### Core Technologies
- **Language**: Kotlin 1.9+ with coroutines and Flow
- **UI Framework**: Jetpack Compose with Material Design 3
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Dagger Hilt
- **Backend**: Supabase (PostgreSQL, Auth, Storage)

### Key Libraries
- **Navigation**: Jetpack Navigation Compose
- **State Management**: ViewModel + StateFlow
- **Image Loading**: Coil for Compose
- **Testing**: JUnit 5, MockK, Compose UI testing
- **Logging**: Timber for debug logging

## 🏗 Application Architecture

```
src/main/java/com/example/carfinder/
├── data/                          # Data Layer
│   ├── local/                    # Local data sources
│   │   ├── database/            # Room database entities
│   │   └── preferences/         # SharedPreferences
│   ├── remote/                   # Remote data sources
│   │   ├── supabase/            # Supabase API clients
│   │   └── dto/                 # Data Transfer Objects
│   └── repository/               # Repository implementations
├── domain/                       # Domain Layer
│   ├── model/                   # Business models
│   ├── repository/              # Repository interfaces
│   └── usecase/                 # Business use cases
├── presentation/                 # Presentation Layer
│   ├── components/              # Reusable UI components
│   ├── screen/                  # Screen composables
│   ├── theme/                   # Design system
│   ├── navigation/              # Navigation graph
│   └── viewmodel/               # ViewModel classes
└── di/                          # Dependency Injection
    └── modules/                 # Hilt modules
```

## 📱 Screens

### Main Screens
- **Home Screen**: Search interface with recent activity
- **Lost Vehicles**: Browse and manage lost vehicle reports
- **Found Vehicles**: View and update found vehicle listings
- **Vehicle Details**: Comprehensive vehicle information
- **User Profile**: Account management and settings

## 🚀 Getting Started

### Prerequisites

- **Android Studio** (Flamingo 2022.2.1 or later)
- **Android SDK** 33 (API 33) or higher
- **Java 17** or Kotlin 1.9.0+
- **Supabase Account** ([Sign up free](https://supabase.com))

### Step 1: Clone the Repository

```bash
git clone https://github.com/yourusername/car-finder.git
cd car-finder
```

### Step 2: Configure Supabase

1. **Create Supabase Project**
   - Go to [supabase.com](https://supabase.com)
   - Create new project and get your URL and anon key

2. **Set Up Environment Variables**
   Create `local.properties` in root directory:
   ```properties
   supabase.url=https://your-project.supabase.co
   supabase.key=your-supabase-anon-key
   ```

3. **Initialize Database**
   Run the setup script in Supabase SQL editor:
   ```sql
   -- Creates vehicles, users, and search tables
   -- (See database/init.sql for full schema)
   ```

### Step 3: Build and Run

```bash
# Build debug version
./gradlew assembleDebug

# Run tests
./gradlew test
./gradlew connectedAndroidTest

# Or open in Android Studio:
# 1. File > Open > Select project folder
# 2. Click 'Run' button (green play icon)
```

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumentation Tests
```bash
./gradlew connectedAndroidTest
```

### UI Tests
```bash
./gradlew connectedCheck
```

## 📊 Database Schema

The application uses the following main tables:

- **vehicles**: Vehicle information (VIN, license plate, make, model, etc.)
- **users**: User accounts and profiles
- **vehicle_status**: Lost/found status tracking
- **search_history**: User search queries and results

## 🔧 Configuration

### Environment Variables
Set these in `local.properties`:

```properties
# Supabase Configuration
supabase.url=YOUR_SUPABASE_URL
supabase.key=YOUR_SUPABASE_KEY

# Optional: Debug settings
debug.mode=true
log.level=DEBUG
```

### Build Variants
- **debug**: Development build with debug symbols
- **release**: Production build with ProGuard/R8

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. **Fork** the repository
2. **Create a feature branch**: `git checkout -b feature/your-feature`
3. **Commit changes**: `git commit -m 'Add your feature'`
4. **Push to branch**: `git push origin feature/your-feature`
5. **Open Pull Request**

### Contribution Guidelines
- Follow Kotlin coding conventions
- Write unit tests for new features
- Update documentation when needed
- Use descriptive commit messages

## 🐛 Troubleshooting

### Common Issues

**Build Errors**
- Ensure Android SDK is up to date
- Check `local.properties` for correct Supabase credentials
- Clean and rebuild project: `./gradlew clean build`

**Supabase Connection**
- Verify Supabase project is active
- Check internet connection
- Validate API keys in configuration

## 📄 License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

## 🙏 Attribution

When using this code, please provide proper attribution:

```
Based on Car Finder App developed by [Your Name]
Repository: https://github.com/yourusername/car-finder
```

For commercial use or custom licensing, please contact the author.

## 📞 Support

- **GitHub Issues**: [Report bugs or feature requests](https://github.com/yourusername/car-finder/issues)
- **Documentation**: Check the [wiki](https://github.com/yourusername/car-finder/wiki) for detailed guides
- **Email**: support@youremail.com

## 🔗 Useful Links

- [Supabase Documentation](https://supabase.com/docs)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [Material Design 3](https://m3.material.io/)

---

**Built with modern Android development practices and ❤️**