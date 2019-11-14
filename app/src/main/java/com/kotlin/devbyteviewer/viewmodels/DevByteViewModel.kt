package com.kotlin.devbyteviewer.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin.devbyteviewer.domain.DevByteVideo
import com.kotlin.devbyteviewer.network.DevByteNetwork
import com.kotlin.devbyteviewer.network.asDomainModel
import kotlinx.coroutines.*
import java.io.IOException

class DevByteViewModel(application: Application) : AndroidViewModel(application) {

    // SOS: a plain Job fails when one of its children fails and the rest of the children are cancelled.
    // w SupervisorJob, a child failure doesn't affect the other children nor the parent.
    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _playlist = MutableLiveData<List<DevByteVideo>>()
    val playlist: LiveData<List<DevByteVideo>>
        get() = _playlist

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() = viewModelScope.launch {

        try {
             val playlist = DevByteNetwork.devbyteService.getPlaylistAsync().await()
            _playlist.postValue(playlist.asDomainModel())
            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false

        } catch (networkError: IOException) {
            _eventNetworkError.value = true
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    // The factory for this viewModel
    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DevByteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DevByteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
