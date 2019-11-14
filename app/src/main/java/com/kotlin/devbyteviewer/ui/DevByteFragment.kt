package com.kotlin.devbyteviewer.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin.devbyteviewer.R
import com.kotlin.devbyteviewer.databinding.FragmentDevByteBinding
import com.kotlin.devbyteviewer.domain.DevByteVideo
import com.kotlin.devbyteviewer.viewmodels.DevByteViewModel
import kotlinx.android.synthetic.main.fragment_dev_byte.view.*

class DevByteFragment : Fragment() {

    private val viewModel: DevByteViewModel by lazy {
        val activity = requireNotNull(this.activity) { "You can only access the viewModel after onActivityCreated()" }
        ViewModelProvider(this, DevByteViewModel.Factory(activity.application))
                .get(DevByteViewModel::class.java)
    }

    // adapter of the recyclerview
    private var devByteAdapter: DevByteAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentDevByteBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_dev_byte, container, false)

        binding.viewModel = viewModel
        // SOS: see below for why we use viewLifecycleOwner here instead of this
        binding.lifecycleOwner = viewLifecycleOwner

        devByteAdapter = DevByteAdapter(VideoClick {
            // SOS: great idiom!
            val packageManager = context?.packageManager ?: return@VideoClick

            var intent = Intent(Intent.ACTION_VIEW, it.youtubeUri)
            if (intent.resolveActivity(packageManager) == null) {
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            }

            startActivity(intent)
        })

        binding.root.recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = devByteAdapter
        }

        // SOS: The reason we use viewLifecycleOwner instead of 'this' here and above: if the fragment
        // is retained, its lifecycle does not end. Its view is destroyed and recreated, thus calling
        // onCreateView again. So we'd keep adding the same Observer again and again! There are other
        // solutions to this, but this is the cleanest
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer { isNetworkError ->
            if (isNetworkError) {
                onNetworkError()
            }
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.playlist.observe(viewLifecycleOwner, Observer { videos ->
            videos?.apply {
                devByteAdapter?.videos = videos
            }
        })
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    // Helper extension property to generate links to the YouTube app
    private val DevByteVideo.youtubeUri: Uri
        get() {
            val httpUri = Uri.parse(url)
            return Uri.parse("vnd.youtube:" + httpUri.getQueryParameter("v"))
        }
}

