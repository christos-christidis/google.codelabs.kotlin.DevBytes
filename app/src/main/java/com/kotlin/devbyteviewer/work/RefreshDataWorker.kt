package com.kotlin.devbyteviewer.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kotlin.devbyteviewer.BuildConfig
import com.kotlin.devbyteviewer.database.VideosDatabase
import com.kotlin.devbyteviewer.repository.VideosRepository
import retrofit2.HttpException
import timber.log.Timber

class RefreshDataWorker(appContext: Context, params: WorkerParameters)
    : CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = BuildConfig.APPLICATION_ID + "work.RefreshDataWorker"
    }

    // SOS: called on background thread. Has 10 minutes to return Result, otherwise it's stopped
    override suspend fun doWork(): Result {
        val database = VideosDatabase.getDatabase(applicationContext)
        val repository = VideosRepository(database)
        try {
            repository.refreshVideos()
            Timber.d("Work request for sync is run")
        } catch (e: HttpException) {
            return Result.retry()
        }

        return Result.success()
    }
}