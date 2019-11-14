package com.kotlin.devbyteviewer.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kotlin.devbyteviewer.domain.DevByteVideo

@Entity
data class DatabaseVideo(
        @PrimaryKey
        val url: String,
        val updated: String,
        val title: String,
        val description: String,
        val thumbnail: String)

fun List<DatabaseVideo>.asDomainModel(): List<DevByteVideo> {
    return map {
        DevByteVideo(
                url = it.url,
                title = it.title,
                description = it.description,
                updated = it.updated,
                thumbnail = it.thumbnail)
    }
}
