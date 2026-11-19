package com.github.libretube.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.github.libretube.helpers.NavigationHelper
import com.github.libretube.parcelable.PlayerData
import com.github.libretube.R
import com.github.libretube.databinding.FragmentHomeBinding
import com.github.libretube.hyperui.bridge.HyperAppShell

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHomeBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                HyperAppShell(
                    onVideoPlay = { videoId ->
                        // Seamless bridge into native player pipeline via NavigationHelper
                        NavigationHelper.navigateVideo(requireContext(), PlayerData(videoId))
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
