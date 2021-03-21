## [Android Sunflower](https://github.com/android/sunflower)

A gardening app illustrating Android development best practices with Android Jetpack.

Android Sunflower is currently under heavy development. Note that some changes (such as database schema modifications) are not backwards compatible and may cause the app to crash. In this case, please uninstall and re-install the app.

### Introduction

Android Jetpack is a set of components, tools and guidance to make great Android apps. They bring together the existing Support Library and Architecture Components and arrange them into four categories:

[![Android Jetpack](https://github.com/android/sunflower/raw/main/screenshots/jetpack_donut.png)](https://github.com/android/sunflower/blob/main/screenshots/jetpack_donut.png)

Android Sunflower demonstrates utilizing these components to create a simple gardening app. Read the [Introducing Android Sunflower](https://medium.com/androiddevelopers/introducing-android-sunflower-e421b43fe0c2) article for a walkthrough of the app.

### Getting Started

This project uses the Gradle build system. To build this project, use the `gradlew build` command or use "Import Project" in Android Studio.

There are two Gradle tasks for testing the project:

- `connectedAndroidTest` - for running Espresso on a connected device
- `test` - for running unit tests

For more resources on learning Android development, visit the [Developer Guides](https://developer.android.com/guide/) at [developer.android.com](https://developer.android.com/).

### Unsplash API key

Sunflower uses the [Unsplash API](https://unsplash.com/developers) to load pictures on the gallery screen. To use the API, you will need to obtain a free developer API key. See the [Unsplash API Documentation](https://unsplash.com/documentation) for instructions.

Once you have the key, add this line to the `gradle.properties` file, either in your user home directory (usually `~/.gradle/gradle.properties` on Linux and Mac) or in the project's root folder:

```
unsplash_access_key=<your Unsplash access key>
```

The app is still usable without an API key, though you won't be able to navigate to the gallery screen.

### Screenshots

[![List of plants](https://github.com/android/sunflower/raw/main/screenshots/phone_plant_list.png)](https://github.com/android/sunflower/blob/main/screenshots/phone_plant_list.png) [![Plant details](https://github.com/android/sunflower/raw/main/screenshots/phone_plant_detail.png)](https://github.com/android/sunflower/blob/main/screenshots/phone_plant_detail.png) [![My Garden](https://github.com/android/sunflower/raw/main/screenshots/phone_my_garden.png)](https://github.com/android/sunflower/blob/main/screenshots/phone_my_garden.png)

### Libraries Used

- Foundation - Components for core system capabilities, Kotlin extensions and support for multidex and automated testing.
  - [AppCompat](https://developer.android.com/topic/libraries/support-library/packages#v7-appcompat) - Degrade gracefully on older versions of Android.
  - [Android KTX](https://developer.android.com/kotlin/ktx) - Write more concise, idiomatic Kotlin code.
  - [Test](https://developer.android.com/training/testing/) - An Android testing framework for unit and runtime UI tests.
- Architecture - A collection of libraries that help you design robust, testable, and maintainable apps. Start with classes for managing your UI component lifecycle and handling data persistence.
  - [Data Binding](https://developer.android.com/topic/libraries/data-binding/) - Declaratively bind observable data to UI elements.
  - [Lifecycles](https://developer.android.com/topic/libraries/architecture/lifecycle) - Create a UI that automatically responds to lifecycle events.
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Build data objects that notify views when the underlying database changes.
  - [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) - Handle everything needed for in-app navigation.
  - [Room](https://developer.android.com/topic/libraries/architecture/room) - Access your app's SQLite database with in-app objects and compile-time checks.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Store UI-related data that isn't destroyed on app rotations. Easily schedule asynchronous tasks for optimal execution.
  - [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) - Manage your Android background jobs.
- UI - Details on why and how to use UI Components in your apps - together or separate
  - [Animations & Transitions](https://developer.android.com/training/animation/) - Move widgets and transition between screens.
  - [Fragment](https://developer.android.com/guide/components/fragments) - A basic unit of composable UI.
  - [Layout](https://developer.android.com/guide/topics/ui/declaring-layout) - Lay out widgets using different algorithms.
- Third party and miscellaneous libraries
  - [Glide](https://bumptech.github.io/glide/) for image loading
  - [Hilt](https://developer.android.com/training/dependency-injection/hilt-android): for [dependency injection](https://developer.android.com/training/dependency-injection)
  - [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) for managing background threads with simplified code and reducing needs for callbacks