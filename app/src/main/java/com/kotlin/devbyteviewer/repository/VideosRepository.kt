package com.kotlin.devbyteviewer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.kotlin.devbyteviewer.database.VideosDatabase
import com.kotlin.devbyteviewer.database.asDomainModel
import com.kotlin.devbyteviewer.domain.DevByteVideo
import com.kotlin.devbyteviewer.network.DevByteNetwork
import com.kotlin.devbyteviewer.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class VideosRepository(private val database: VideosDatabase) {

    // SOS: this is the right (only?) way to get LiveData of a different type from the original LiveData
    val videos: LiveData<List<DevByteVideo>> = Transformations.map(database.videoDao.getVideos()) {
        it.asDomainModel()
    }

    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            Timber.d("refresh videos is called");
            val playlist = DevByteNetwork.devbyteService.getPlaylistAsync().await()
            database.videoDao.insertAll(playlist.asDatabaseModel())
        }
    }
}
