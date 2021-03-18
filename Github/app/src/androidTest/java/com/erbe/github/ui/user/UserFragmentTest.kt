package com.erbe.github.ui.user

import androidx.databinding.DataBindingComponent
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.erbe.github.R
import com.erbe.github.binding.FragmentBindingAdapters
import com.erbe.github.util.CountingAppExecutorsRule
import com.erbe.github.util.DataBindingIdlingResourceRule
import com.erbe.github.util.RecyclerViewMatcher
import com.erbe.github.util.TaskExecutorWithIdlingResourceRule
import com.erbe.github.util.TestUtil
import com.erbe.github.util.ViewModelUtil
import com.erbe.github.util.disableProgressBarAnimations
import com.erbe.github.util.mock
import com.erbe.github.vo.Repo
import com.erbe.github.vo.Resource
import com.erbe.github.vo.User
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class UserFragmentTest {
    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()

    @Rule
    @JvmField
    val countingAppExecutors = CountingAppExecutorsRule()

    @Rule
    @JvmField
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule()
    private lateinit var viewModel: UserViewModel
    private lateinit var mockBindingAdapter: FragmentBindingAdapters
    private val navController = mock<NavController>()
    private val userData = MutableLiveData<Resource<User>>()
    private val repoListData = MutableLiveData<Resource<List<Repo>>>()

    @Before
    fun init() {
        viewModel = mock(UserViewModel::class.java)
        `when`(viewModel.user).thenReturn(userData)
        `when`(viewModel.repositories).thenReturn(repoListData)
        doNothing().`when`(viewModel).setLogin(anyString())
        mockBindingAdapter = mock(FragmentBindingAdapters::class.java)

        val scenario = launchFragmentInContainer(
            UserFragmentArgs("foo").toBundle()
        ) {
            UserFragment().apply {
                appExecutors = countingAppExecutors.appExecutors
                viewModelFactory = ViewModelUtil.createFor(viewModel)
                dataBindingComponent = object : DataBindingComponent {
                    override fun getFragmentBindingAdapters(): FragmentBindingAdapters {
                        return mockBindingAdapter
                    }
                }
            }
        }
        dataBindingIdlingResourceRule.monitorFragment(scenario)
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
            fragment.binding.repoList.itemAnimator = null
            fragment.disableProgressBarAnimations()
        }
    }

    @Test
    fun loading() {
        userData.postValue(Resource.loading(null))
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())))
    }

    @Test
    fun error() {
        doNothing().`when`(viewModel).retry()
        userData.postValue(Resource.error("wtf", null))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.error_msg)).check(matches(withText("wtf")))
        onView(withId(R.id.retry)).check(matches(isDisplayed()))
        onView(withId(R.id.retry)).perform(click())
        verify(viewModel).retry()
    }

    @Test
    fun loadingWithUser() {
        val user = TestUtil.createUser("foo")
        userData.postValue(Resource.loading(user))
        onView(withId(R.id.name)).check(matches(withText(user.name)))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun loadedUser() {
        val user = TestUtil.createUser("foo")
        userData.postValue(Resource.success(user))
        onView(withId(R.id.name)).check(matches(withText(user.name)))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun loadRepos() {
        val repos = setRepos(2)
        for (pos in repos.indices) {
            val repo = repos[pos]
            onView(listMatcher().atPosition(pos)).apply {
                check(matches(hasDescendant(withText(repo.name))))
                check(matches(hasDescendant(withText(repo.description))))
                check(matches(hasDescendant(withText(repo.stars.toString()))))
            }
        }
        val repo3 = setRepos(3)[2]
        onView(listMatcher().atPosition(2)).check(
            matches(hasDescendant(withText(repo3.name)))
        )
    }

    @Test
    fun clickRepo() {
        val repos = setRepos(2)
        val selected = repos[1]
        onView(withText(selected.description)).perform(click())
        verify(navController).navigate(
            UserFragmentDirections.showRepo(selected.owner.login, selected.name)
        )
    }

    @Test
    fun nullUser() {
        userData.postValue(null)
        onView(withId(R.id.name)).check(matches(not(isDisplayed())))
    }

    @Test
    fun nullRepoList() {
        repoListData.postValue(null)
        onView(listMatcher().atPosition(0)).check(doesNotExist())
    }

    @Test
    fun nulledUser() {
        val user = TestUtil.createUser("foo")
        userData.postValue(Resource.success(user))
        onView(withId(R.id.name)).check(matches(withText(user.name)))
        userData.postValue(null)
        onView(withId(R.id.name)).check(matches(not(isDisplayed())))
    }

    @Test
    fun nulledRepoList() {
        setRepos(5)
        onView(listMatcher().atPosition(1)).check(matches(isDisplayed()))
        repoListData.postValue(null)
        onView(listMatcher().atPosition(0)).check(doesNotExist())
    }

    private fun listMatcher() = RecyclerViewMatcher(R.id.repo_list)

    private fun setRepos(count: Int): List<Repo> {
        val repos = (0 until count).map {
            TestUtil.createRepo("foo", "name $it", "desc$it")
        }
        repoListData.postValue(Resource.success(repos))
        return repos
    }
}