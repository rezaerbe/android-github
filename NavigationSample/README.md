## [Android Architecture Components Navigation Basic Sample](https://github.com/android/architecture-components-samples/tree/master/NavigationBasicSample)

### Features

This sample showcases the following features of the Navigation component:

- Navigating via actions
- Transitions
- Popping destinations from the back stack
- Arguments (profile screen receives a user name)
- Deep links (`www.example.com/user/{user name}` opens the profile screen)

### Screenshots

[![Screenshot](https://github.com/android/architecture-components-samples/raw/main/NavigationBasicSample/screenshot.png)](https://github.com/android/architecture-components-samples/blob/main/NavigationBasicSample/screenshot.png)

### Other Resources

- Particularly In Java, consider using `Navigation.createNavigateOnClickListener()` to quickly create click listeners.
- Consider including the [Navigation KTX libraries](https://developer.android.com/topic/libraries/architecture/adding-components#navigation) for more concise uses of the Navigation component. For example, calls to `Navigation.findNavController(view)` can be expressed as `view.findNavController()`.

### Tests

This sample contains UI tests that can be run on device (or emulator) or on the host (as a JVM test, using Robolectric). As of Android Studio (3.3.1), running these tests will default to Android Instrumented test (device or emulator). In order to run them with Robolectric you have two options:

- From the command line, running `./gradlew test`
- From Android Studio, creating a new "Android JUnit" run configuration and targeting "all in package" or a specific class or directory.