## [Android Architecture Blueprints v2](https://github.com/android/architecture-samples)

[![Illustration by Virginia Poltrack](https://github.com/googlesamples/android-architecture/wiki/images/aab-logov2.png)](https://github.com/googlesamples/android-architecture/wiki/images/aab-logov2.png)

Android Architecture Blueprints is a project to showcase different architectural approaches to developing Android apps. In its different branches you'll find the same app (a TODO app) implemented with small differences.

In this branch you'll find:

- Kotlin **[Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)** for background operations.
- A single-activity architecture, using the **[Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started)** to manage fragment operations.
- A presentation layer that contains a fragment (View) and a **ViewModel** per screen (or feature).
- Reactive UIs using **LiveData** observables and **Data Binding**.
- A **data layer** with a repository and two data sources (local using Room and remote) that are queried with one-shot operations (no listeners or data streams).
- Two **product flavors**, `mock` and `prod`, [to ease development and testing](https://android-developers.googleblog.com/2015/12/leveraging-product-flavors-in-android.html) (except in the Dagger branch).
- A collection of unit, integration and e2e **tests**, including "shared" tests that can be run on emulator/device or Robolectric.

### Why a to-do app?

[![A demo illustraating the UI of the app](https://github.com/googlesamples/android-architecture/wiki/images/todoapp.gif)](https://github.com/googlesamples/android-architecture/wiki/images/todoapp.gif)

The app in this project aims to be simple enough that you can understand it quickly, but complex enough to showcase difficult design decisions and testing scenarios. For more information, see the [app's specification](https://github.com/googlesamples/android-architecture/wiki/To-do-app-specification).

### What is it not?

- A UI/Material Design sample. The interface of the app is deliberately kept simple to focus on architecture. Check out [Plaid](https://github.com/android/plaid) instead.
- A complete Jetpack sample covering all libraries. Check out [Android Sunflower](https://github.com/googlesamples/android-sunflower) or the advanced [Github Browser Sample](https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample) instead.
- A real production app with network access, user authentication, etc. Check out the [Google I/O app](https://github.com/google/iosched), [Santa Tracker](https://github.com/google/santa-tracker-android) or [Tivi](https://github.com/chrisbanes/tivi) for that.

### Who is it for?

- Intermediate developers and beginners looking for a way to structure their app in a testable and maintainable way.
- Advanced developers looking for quick reference.

### Summary

This sample is written in Kotlin and based on the [master](https://github.com/googlesamples/android-architecture/tree/master) branch which uses the following Architecture Components:

- ViewModel
- LiveData
- Data Binding
- Navigation
- Room

It uses [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency injection.

### Differences with master

- The ServiceLocator class is removed. Object creation and scoping is handled by Hilt.
- Flavors `mock` and `prod` are no longer needed for testing so they're removed.

### Testing

UI tests don't rely on using the `mock` flavor to run quickly and hermetically. Instead, they use Hilt to provide their test versions.

This is done by creating a `CustomTestRunner` that uses an `Application` configured with Hilt. As per the [Hilt testing documentation](https://developer.android.com/training/dependency-injection/hilt-android), `@HiltAndroidTest` will automatically create the right Hilt components for each test.