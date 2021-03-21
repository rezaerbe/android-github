package com.erbe.github.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.erbe.github.repository.RepoRepository
import com.erbe.github.repository.UserRepository
import com.erbe.github.testing.OpenForTesting
import com.erbe.github.util.AbsentLiveData
import com.erbe.github.vo.Repo
import com.erbe.github.vo.Resource
import com.erbe.github.vo.User
import javax.inject.Inject

@OpenForTesting
class UserViewModel
@Inject constructor(userRepository: UserRepository, repoRepository: RepoRepository) : ViewModel() {
    private val _login = MutableLiveData<String?>()
    val login: LiveData<String?>
        get() = _login
    val repositories: LiveData<Resource<List<Repo>>> = _login.switchMap { login ->
        if (login == null) {
            AbsentLiveData.create()
        } else {
            repoRepository.loadRepos(login)
        }
    }
    val user: LiveData<Resource<User>> = _login.switchMap { login ->
        if (login == null) {
            AbsentLiveData.create()
        } else {
            userRepository.loadUser(login)
        }
    }

    fun setLogin(login: String?) {
        if (_login.value != login) {
            _login.value = login
        }
    }

    fun retry() {
        _login.value?.let {
            _login.value = it
        }
    }
}
