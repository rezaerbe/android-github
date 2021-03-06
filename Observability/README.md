## [Room & RxJava (Kotlin) Sample](https://github.com/android/architecture-components-samples/tree/main/BasicRxJavaSampleKotlin)

This is an API sample to showcase how to use [Room](https://developer.android.com/topic/libraries/architecture/room.html), with RxJava's [Flowable](http://reactivex.io/RxJava/2.x/javadoc/io/reactivex/Flowable.html) objects in Kotlin.

### Introduction

#### Functionality

The sample app shows an editable user name, stored in the database.

#### Implementation

##### Data layer

The database is created using Room and has one entity: a `User`. Room generates the corresponding SQLite table at runtime.

Queries are executed in the `UserDao` class. The user retrieval is done via an observable query implemented using a `Flowable`. Every time the user data is updated, the Flowable object will emit automatically, allowing it to update the UI based on the latest data. The Flowable will emit only when the query result contains at least a row. When there is no data to match the query, the Flowable will not emit.

##### Presentation layer

The app has a main Activity that displays the data. The Activity works with a ViewModel to do the following:

- subscribe to the emissions of the user name and updates the UI every time there is a new user name emitted
- notify the ViewModel when the user pressed the "Update" button and passes the new user name. The ViewModel works with the data source to get and save the data.

Room guarantees that the observable query will be triggered on a background thread. In the Activity, the Flowable events are set to be received on the main thread, so the UI can be updated. The insert query is synchronous so it's wrapped in a Completable and executed on a background thread. On completion, the Activity is notified on the main thread.