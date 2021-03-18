package com.erbe.github.repository

import com.erbe.github.testing.OpenForTesting
import androidx.lifecycle.LiveData
import com.erbe.github.AppExecutors
import com.erbe.github.api.GithubService
import com.erbe.github.db.UserDao
import com.erbe.github.vo.Resource
import com.erbe.github.vo.User
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles User objects.
 */
@OpenForTesting
@Singleton
class UserRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val userDao: UserDao,
    private val githubService: GithubService
) {

    fun loadUser(login: String): LiveData<Resource<User>> {
        return object : NetworkBoundResource<User, User>(appExecutors) {
            override fun saveCallResult(item: User) {
                userDao.insert(item)
            }

            override fun shouldFetch(data: User?) = data == null

            override fun loadFromDb() = userDao.findByLogin(login)

            override fun createCall() = githubService.getUser(login)
        }.asLiveData()
    }
}
