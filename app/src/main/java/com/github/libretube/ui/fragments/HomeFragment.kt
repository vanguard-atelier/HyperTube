package com.github.libretube.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.libretube.NavDirections
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
                        // Seamless bridge into native player pipeline
                        findNavController().navigate(NavDirections.actionGlobalPlayerFragment(videoId = videoId))
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
