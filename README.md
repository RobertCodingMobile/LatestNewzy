# PaginationNews

PaginationNews is a modern Android application built with a focus on high-quality engineering practices, following Google's best recommendations for Android development.

⚠️ **Note**: This project is currently **under development**.

## 🚀 Tech Stack

The application leverages the latest libraries and tools in the Android ecosystem:

*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern declarative UI.
*   **Architecture**: Multi-module Clean Architecture (Domain, Data, App, Common) to ensure scalability and testability.
*   **Dependency Injection**: [Koin](https://insert-koin.io/) - A pragmatic lightweight dependency injection framework.
*   **Networking**: [Ktor](https://ktor.io/) - Asynchronous HTTP client for Kotlin.
*   **Local Database**: [Room](https://developer.android.com/training/data-storage/room) - Persistence library over SQLite.
*   **Pagination**: [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data) - Efficient loading and displaying of large datasets.
*   **Navigation**: [Navigation 3](https://developer.android.com/guide/navigation/navigation-3) - The latest iteration of Jetpack Navigation.
*   **Asynchronous Programming**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html).
*   **Image Loading**: [Coil](https://coil-kt.github.io/coil/) - Kotlin-first image loading library.
*   **Serialization**: [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) - Type-safe JSON parsing.
*   **Dependency Management**: [Gradle Version Catalog](https://developer.android.com/build/migrate-to-catalogs) - Centralized dependency management.

## 🏗️ Architecture

The project is structured into several modules to enforce separation of concerns:

- **`:domain`**: Contains business logic, entities, and repository interfaces (Pure Kotlin/Android Library).
- **`:data`**: Implements the repositories, data sources (Ktor for remote, Room for local), and mappers.
- **`:app`**: The main Android application module containing UI (Compose), ViewModels, and DI configuration.
- **`:common`**: Shared utilities and reusable components across modules.

## 🛠️ Getting Started

1. Clone the repository.
2. Open the project in **Android Studio Ladybug** or newer.
3. Add your NYT API KEY in **NytApiService** class
4. Sync Gradle and run the `:app` module.

## 📈 Future Work

- [ ] Complete implementation of News list with Paging.
- [ ] Implement Offline-first support with Room caching.
- [ ] Add unit and UI tests.
- [ ] Refine UI/UX with Material 3 components.
- [ ] Provide a setting page.
- [ ] Add more screens
- [ ] And many more...
