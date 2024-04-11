package band.effective.hdl.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import band.effective.hdl.domain.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _effect = MutableSharedFlow<MainEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            collectAuthState()
        }
        viewModelScope.launch {
            preferencesRepository.accessToken.collect {
                Log.d("Key", it.toString())
            }
        }
    }

    private suspend fun collectAuthState() {
        preferencesRepository.isAuth.collect { isAuth ->
            Log.d("VM", isAuth.toString())
            if (isAuth == true)
                _effect.emit(MainEffect.OnOpenContentScreen)
            else
                _effect.emit(MainEffect.OnOpenAuthScreen)

        }
    }

    override fun onCleared() {
        Log.d("VM", "DESTROY VM")
        super.onCleared()
    }
}