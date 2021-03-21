package com.erbe.github.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.erbe.github.api.GithubService
import com.erbe.github.db.UserDao
import com.erbe.github.util.ApiUtil
import com.erbe.github.util.InstantAppExecutors
import com.erbe.github.util.TestUtil
import com.erbe.github.util.mock
import com.erbe.github.vo.Resource
import com.erbe.github.vo.User
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class UserRepositoryTest {
    private val userDao = mock(UserDao::class.java)
    private val githubService = mock(GithubService::class.java)
    private val repo = UserRepository(InstantAppExecutors(), userDao, githubService)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun loadUser() {
        repo.loadUser("abc")
        verify(userDao).findByLogin("abc")
    }

    @Test
    fun goToNetwork() {
        val dbData = MutableLiveData<User>()
        `when`(userDao!!.findByLogin("foo")).thenReturn(dbData)
        val user = TestUtil.createUser("foo")
        val call = ApiUtil.successCall(user)
        `when`(githubService!!.getUser("foo")).thenReturn(call)
        val observer = mock<Observer<Resource<User>>>()

        repo.loadUser("foo").observeForever(observer)
        verify(githubService, never()).getUser("foo")
        val updatedDbData = MutableLiveData<User>()
        `when`(userDao.findByLogin("foo")).thenReturn(updatedDbData)
        dbData.value = null
        verify(githubService).getUser("foo")
    }

    @Test
    fun dontGoToNetwork() {
        val dbData = MutableLiveData<User>()
        val user = TestUtil.createUser("foo")
        dbData.value = user
        `when`(userDao!!.findByLogin("foo")).thenReturn(dbData)
        val observer = mock<Observer<Resource<User>>>()
        repo.loadUser("foo").observeForever(observer)
        verify(githubService, never()).getUser("foo")
        verify(observer).onChanged(Resource.success(user))
    }
}