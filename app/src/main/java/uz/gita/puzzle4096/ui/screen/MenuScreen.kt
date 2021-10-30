package uz.gita.puzzle4096.ui.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import uz.gita.puzzle4096.R
import uz.gita.puzzle4096.databinding.ScreenMenuBinding

class MenuScreen : Fragment(R.layout.screen_menu) {
    private var _binding: ScreenMenuBinding? = null
    private val binding get() = _binding!!
    private var animation: LottieAnimationView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ScreenMenuBinding.bind(view)
        animation = binding.animationView

        binding.playBtn.setOnClickListener {
            findNavController().navigate(R.id.action_menuScreen_to_mainScreen)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        animation = null
        _binding = null
    }
}