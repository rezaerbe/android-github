## [Github Browser Sample with Android Architecture Components](https://github.com/android/architecture-components-samples/tree/master/GithubBrowserSample)

This is a sample app that uses Android Architecture Components with Dagger 2.

**NOTE** It is a relatively more complex and complete example so if you are not familiar with [Architecture Components](https://developer.android.com/arch), you are highly recommended to check other examples in this repository first.

### Introduction

#### Functionality

The app is composed of 3 main screens.

##### SearchFragment

Allows you to search repositories on Github. Each search result is kept in the database in `RepoSearchResult` table where the list of repository IDs are denormalized into a single column. The actual `Repo` instances live in the `Repo` table.

Each time a new page is fetched, the same `RepoSearchResult` record in the Database is updated with the new list of repository ids.

**NOTE** The UI currently loads all `Repo` items at once, which would not perform well on lower end devices. Instead of manually writing lazy adapters, we've decided to wait until the built in support in Room is released.

##### RepoFragment

This fragment displays the details of a repository and its contributors.

##### UserFragment

This fragment displays a user and their repositories.

#### Building

You can open the project in Android studio and press run.

#### Testing

The project uses both instrumentation tests that run on the device and local unit tests that run on your computer. To run both of them and generate a coverage report, you can run:

`./gradlew fullCoverageReport` (requires a connected device or an emulator)

##### Device Tests

###### UI Tests

The projects uses Espresso for UI testing. Since each fragment is limited to a ViewModel, each test mocks related ViewModel to run the tests.

###### Database Tests

The project creates an in memory database for each database test but still runs them on the device.

##### Local Unit Tests

###### ViewModel Tests

Each ViewModel is tested using local unit tests with mock Repository implementations.

###### Repository Tests

Each Repository is tested using local unit tests with mock web service and mock database.

###### Webservice Tests

The project uses [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) project to test REST api interactions.

#### Libraries

- [Android Support Library](https://developer.android.com/topic/libraries/support-library/index.html)
- [Android Architecture Components](https://developer.android.com/arch)
- [Android Data Binding](https://developer.android.com/topic/libraries/data-binding/index.html)
- [Dagger 2](https://google.github.io/dagger) for dependency injection
- [Retrofit](http://square.github.io/retrofit) for REST api communication
- [Glide](https://github.com/bumptech/glide) for image loading
- [Timber](https://github.com/JakeWharton/timber) for logging
- [espresso](https://google.github.io/android-testing-support-library/docs/espresso/) for UI tests
- [mockito](http://site.mockito.org/) for mocking in tests