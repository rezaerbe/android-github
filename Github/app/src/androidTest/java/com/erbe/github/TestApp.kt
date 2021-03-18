package com.erbe.github

import android.app.Application

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 *
 * See [com.erbe.github.util.GithubTestRunner].
 */
class TestApp : Application()
