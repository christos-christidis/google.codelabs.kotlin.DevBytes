package com.kotlin.devbyteviewer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.devbyteviewer.R
import com.kotlin.devbyteviewer.databinding.DevbyteItemBinding
import com.kotlin.devbyteviewer.domain.DevByteVideo

class VideoClick(val block: (DevByteVideo) -> Unit) {
    fun onClick(video: DevByteVideo) = block(video)
}

class DevByteAdapter(private val clickListener: VideoClick) : RecyclerView.Adapter<DevByteAdapter.DevByteViewHolder>() {

    var videos: List<DevByteVideo> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevByteViewHolder {
        val withDataBinding: DevbyteItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), DevByteViewHolder.LAYOUT, parent, false)

        return DevByteViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: DevByteViewHolder, position: Int) {
        holder.binding.apply {
            video = videos[position]
            videoClick = clickListener
        }
    }

    override fun getItemCount() = videos.size

    class DevByteViewHolder(val binding: DevbyteItemBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.devbyte_item
        }
    }
}
