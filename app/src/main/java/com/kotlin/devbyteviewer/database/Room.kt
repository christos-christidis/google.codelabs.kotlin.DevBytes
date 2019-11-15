package com.kotlin.devbyteviewer.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface VideoDao {

    @Query("select * from databasevideo")
    fun getVideos(): LiveData<List<DatabaseVideo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(videos: List<DatabaseVideo>)
}

private lateinit var INSTANCE: VideosDatabase

@Database(entities = [DatabaseVideo::class], version = 1)
abstract class VideosDatabase : RoomDatabase() {

    abstract val videoDao: VideoDao

    companion object {
        fun getDatabase(context: Context): VideosDatabase {
            synchronized(VideosDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            VideosDatabase::class.java,
                            "videos").build()
                }
            }
            return INSTANCE
        }
    }
}

