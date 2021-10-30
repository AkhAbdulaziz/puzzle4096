package uz.gita.puzzle4096.ui.screen

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import uz.gita.puzzle4096.R
import uz.gita.puzzle4096.databinding.ScreenResultBinding
import uz.gita.puzzle4096.utils.format

class ResultScreen : Fragment(R.layout.screen_result) {
    private var _binding: ScreenResultBinding? = null
    private val binding get() = _binding!!
    private var myScore: Long = 2
    private var myScoreStr: String = "$myScore"
    private var doWeCelebrate = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ScreenResultBinding.bind(view)
        arguments?.let {
            val resultTitle = it.getString("RESULT_TITLE", "Your Score")
            if (resultTitle.equals("Congratulations!")) {
                myScoreStr = "You Win"
                doWeCelebrate = true
                binding.resultTitle.text = resultTitle
            } else {
                myScore = it.getLong("MY_SCORE", 2)
                if (myScore < 1000) {
                    myScoreStr = "$myScore".format(2)
                } else
                    if (myScore in 1001..999999999) {
                        myScoreStr = "${(myScore / 1000.0).format(2)}K"
                    } else if (myScore > 1000_000_000) {
                        myScoreStr = "${(myScore / 1000_000_000.0).format(2)}M"
                    }
            }
            binding.myScoreText.text = myScoreStr
        }

        binding.btnNew.setOnClickListener {
            val mainScreen = MainScreen()
            val bundle = Bundle()
            bundle.putInt("START_NEW_GAME", 1)
            mainScreen.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.screenMainContainer, mainScreen)
                .commit()
//            findNavController().navigate(R.id.action_resultScreen_to_mainScreen, bundle)
        }

        binding.btnExit.setOnClickListener {
            requireActivity().finish()
        }
        if (doWeCelebrate) {
            binding.viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(10000L)
                .addShapes(Shape.Square, Shape.Circle)
                .addSizes(Size(12))
                .setPosition(-50f, binding.viewKonfetti.width + 50f, -50f, -50f)
                .streamFor(300, 5000L)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}