## [Paging With Network Sample](https://github.com/android/architecture-components-samples/tree/master/PagingWithNetworkSample)

This sample demonstrates how to use the Paging library with a backend API (in this case [Reddit API](https://www.reddit.com/dev/api/#listings)).

There are 3 variations of the demo, which you can select in the `MainActivity` class.

After selecting an option, it starts the `RedditActivity` which is the activity that shows the list of posts in a given subreddit.

### Paging With Database And Network

This sample, implemented in the [DbRedditPostRepository](https://github.com/android/architecture-components-samples/blob/main/PagingWithNetworkSample/app/src/main/java/com/android/example/paging/pagingwithnetwork/reddit/repository/inDb/DbRedditPostRepository.kt) class, demonstrates how to set up a Repository that will use the local database to page in data for the UI and also back-fill the database from the network as the user reaches to the end of the data in the database.

It uses `Room` to create the `PagingSource` ([dao](https://github.com/android/architecture-components-samples/blob/main/PagingWithNetworkSample/app/src/main/java/com/android/example/paging/pagingwithnetwork/reddit/db/RedditPostDao.kt)). The `Pager` creates a stream of data from the PagingSource to the UI, and more data is paged in as it is consumed.

This usually provides the best user experience as the cached content is always available on the device and the user will still have a good experience even if the network is slow / unavailable.

### Paging Using Item Keys

This sample, implemented in the [InMemoryByItemRepository](https://github.com/android/architecture-components-samples/blob/main/PagingWithNetworkSample/app/src/main/java/com/android/example/paging/pagingwithnetwork/reddit/repository/inMemory/byItem/InMemoryByItemRepository.kt) class, demonstrates how to set up a Repository that will directly page in from the network and will use the `key` from the previous item to find the request parameters for the next page.

[ItemKeyedSubredditPagingSource](https://github.com/android/architecture-components-samples/blob/main/PagingWithNetworkSample/app/src/main/java/com/android/example/paging/pagingwithnetwork/reddit/repository/inMemory/byItem/ItemKeyedSubredditPagingSource.kt): The data source that uses the `key` in items (`name` in Reddit API) to find the next page. It extends from the `PagingSource` class in the Paging Library.

### Paging Using Next Tokens From The Previous Query

This sample, implemented in the [InMemoryByPageKeyRepository](https://github.com/android/architecture-components-samples/blob/main/PagingWithNetworkSample/app/src/main/java/com/android/example/paging/pagingwithnetwork/reddit/repository/inMemory/byPage/InMemoryByPageKeyRepository.kt) class, demonstrates how to utilize the `before` and `after` keys in the response to discover the next page. (This is the intended use of the Reddit API but this sample still provides [ItemKeyedSubredditPagingSource](https://github.com/android/architecture-components-samples/blob/main/PagingWithNetworkSample/app/src/main/java/com/android/example/paging/pagingwithnetwork/reddit/repository/inMemory/byItem/ItemKeyedSubredditPagingSource.kt) to serve as an example if the backend does not provide before/after links)

[PageKeyedSubredditPagingSource](https://github.com/android/architecture-components-samples/blob/main/PagingWithNetworkSample/app/src/main/java/com/android/example/paging/pagingwithnetwork/reddit/repository/inMemory/byPage/PageKeyedSubredditPagingSource.kt): The data source that uses the `after` and `before` fields in the API request response. It extends from the `PagingSource` class in the Paging Library.

#### Libraries

- [Android Support Library](https://developer.android.com/topic/libraries/support-library/index.html)
- [Android Architecture Components](https://developer.android.com/arch)
- [Retrofit](http://square.github.io/retrofit) for REST api communication
- [Glide](https://github.com/bumptech/glide) for image loading
- [espresso](https://google.github.io/android-testing-support-library/docs/espresso/) for UI tests
- [mockito](http://site.mockito.org/) for mocking in tests
- [Retrofit Mock](https://github.com/square/retrofit/tree/master/retrofit-mock) for creating a fake API implementation for tests