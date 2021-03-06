package com.erbe.github.ui.search


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.erbe.github.repository.RepoRepository
import com.erbe.github.util.mock
import com.erbe.github.vo.Repo
import com.erbe.github.vo.Resource
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class SearchViewModelTest {
    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()
    private val repository = mock(RepoRepository::class.java)
    private lateinit var viewModel: SearchViewModel

    @Before
    fun init() {
        // need to init after instant executor rule is established.
        viewModel = SearchViewModel(repository)
    }

    @Test
    fun empty() {
        val result = mock<Observer<Resource<List<Repo>>>>()
        viewModel.results.observeForever(result)
        viewModel.loadNextPage()
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun basic() {
        val result = mock<Observer<Resource<List<Repo>>>>()
        viewModel.results.observeForever(result)
        viewModel.setQuery("foo")
        verify(repository).search("foo")
        verify(repository, never()).searchNextPage("foo")
    }

    @Test
    fun noObserverNoQuery() {
        `when`(repository.searchNextPage("foo")).thenReturn(mock())
        viewModel.setQuery("foo")
        verify(repository, never()).search("foo")
        // next page is user interaction and even if loading state is not observed, we query
        // would be better to avoid that if main search query is not observed
        viewModel.loadNextPage()
        verify(repository).searchNextPage("foo")
    }

    @Test
    fun swap() {
        val nextPage = MutableLiveData<Resource<Boolean>>()
        `when`(repository.searchNextPage("foo")).thenReturn(nextPage)

        val result = mock<Observer<Resource<List<Repo>>>>()
        viewModel.results.observeForever(result)
        verifyNoMoreInteractions(repository)
        viewModel.setQuery("foo")
        verify(repository).search("foo")
        viewModel.loadNextPage()

        viewModel.loadMoreStatus.observeForever(mock())
        verify(repository).searchNextPage("foo")
        assertThat(nextPage.hasActiveObservers(), `is`(true))
        viewModel.setQuery("bar")
        assertThat(nextPage.hasActiveObservers(), `is`(false))
        verify(repository).search("bar")
        verify(repository, never()).searchNextPage("bar")
    }

    @Test
    fun refresh() {
        viewModel.refresh()
        verifyNoMoreInteractions(repository)
        viewModel.setQuery("foo")
        viewModel.refresh()
        verifyNoMoreInteractions(repository)
        viewModel.results.observeForever(mock())
        verify(repository).search("foo")
        reset(repository)
        viewModel.refresh()
        verify(repository).search("foo")
    }

    @Test
    fun resetSameQuery() {
        viewModel.results.observeForever(mock())
        viewModel.setQuery("foo")
        verify(repository).search("foo")
        reset(repository)
        viewModel.setQuery("FOO")
        verifyNoMoreInteractions(repository)
        viewModel.setQuery("bar")
        verify(repository).search("bar")
    }
}
