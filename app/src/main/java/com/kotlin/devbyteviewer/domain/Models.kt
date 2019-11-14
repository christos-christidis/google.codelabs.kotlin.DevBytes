package com.kotlin.devbyteviewer.domain

import com.kotlin.devbyteviewer.util.smartTruncate

// Domain objects are plain Kotlin data classes that represent things in our app. These are the
// objects that should be displayed on screen, or manipulated by the app.

data class DevByteVideo(val title: String,
                        val description: String,
                        val url: String,
                        val updated: String,
                        val thumbnail: String) {

    val shortDescription: String
        get() = description.smartTruncate(200)
}