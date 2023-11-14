# Sample Search App Consuming Mercado Libre Services

## Overview

This is a sample Android application that demonstrates the integration with Mercado Libre services. The app showcases the use of various libraries and follows a specific architecture to achieve clean and maintainable code.

## Libraries Used

- [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html): Kotlin's native asynchronous programming library.
- [Android Navigation Component](https://developer.android.com/guide/navigation): Navigation framework for managing navigation and deep linking.
- [Compose](https://developer.android.com/jetpack/compose): Modern Android UI toolkit for building native UIs.
- [MockK](https://mockk.io/): Mocking library for Kotlin.
- [Arrow](https://arrow-kt.io/): Functional programming library for Kotlin.
- [Room](https://developer.android.com/training/data-storage/room): Android's persistence library for SQLite databases.
- [Retrofit](https://square.github.io/retrofit/): Type-safe HTTP client for Android and Java.

## Architecture

The app adheres to the principles of [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) and utilizes the [MVVM (Model-View-ViewModel)](https://developer.android.com/jetpack/guide) architecture pattern. This ensures a clear separation of concerns, promoting modularity and scalability in the codebase.

### Project Structure

- **data:** Contains data-related classes such as repositories, data sources, and mappers.
- **domain:** Holds domain entities, use cases, and repository interfaces.
- **app:** Consists of UI-related classes, including ViewModels, composable functions, and UI components.

### TODOs for Next Version

1. **Separate UI into a Design System Module:**
    - Move UI components, styles, and theme-related code to a separate `design-system` module.

2. **Create a Presentation Module:**
    - Move ViewModels, fragments/activities, and navigation-related components to a new `presentation` module.
    - Focus on keeping this module lightweight and purely concerned with UI logic.
