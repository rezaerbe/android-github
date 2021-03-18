package com.erbe.observability

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.erbe.observability.persistence.User
import com.erbe.observability.persistence.UserDao
import com.erbe.observability.ui.UserViewModel
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * Unit test for [UserViewModel]
 */
class UserViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var dataSource: UserDao

    @Captor
    private lateinit var userArgumentCaptor: ArgumentCaptor<User>

    private lateinit var viewModel: UserViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = UserViewModel(dataSource)
    }

    @Test
    fun getUserName_whenNoUserSaved() {
        // Given that the UserDataSource returns an empty list of users
        `when`(dataSource.getUserById(UserViewModel.USER_ID)).thenReturn(Flowable.empty<User>())

        //When getting the user name
        viewModel.userName()
                .test()
                // The user name is empty
                .assertNoValues()
    }

    @Test
    fun getUserName_whenUserSaved() {
        // Given that the UserDataSource returns a user
        val user = User(userName = "user name")
        `when`(dataSource.getUserById(UserViewModel.USER_ID)).thenReturn(Flowable.just(user))

        //When getting the user name
        viewModel.userName()
                .test()
                // The correct user name is emitted
                .assertValue("user name")
    }

    @Test
    fun updateUserName_updatesNameInDataSource() {
        // Given that a user is already inserted
        dataSource.insertUser(User(UserViewModel.USER_ID, "name"))

        // And a specific user is expected when inserting
        val userName = "new user name"
        val expectedUser = User(UserViewModel.USER_ID, userName)
        `when`(dataSource.insertUser(expectedUser)).thenReturn(Completable.complete())

        // When updating the user name
        viewModel.updateUserName(userName)
                .test()
                .assertComplete()
    }
}