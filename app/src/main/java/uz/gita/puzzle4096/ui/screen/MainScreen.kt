package uz.gita.puzzle4096.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import uz.gita.puzzle4096.R
import uz.gita.puzzle4096.data.MySide
import uz.gita.puzzle4096.databinding.ScreenMainBinding
import uz.gita.puzzle4096.ui.viewmodels.MainViewModel
import uz.gita.puzzle4096.utils.MyTouchListener
import uz.gita.puzzle4096.utils.background
import uz.gita.puzzle4096.utils.format

class MainScreen : Fragment(R.layout.screen_main) {
    private var _binding: ScreenMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainViewModel>()
    private val views = ArrayList<TextView>(16)

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = ScreenMainBinding.bind(view)
        arguments?.let {
            if (it.getInt("START_NEW_GAME") == 1) {
               reloadGame()
            }
        }

        loadView()
        binding.textCurrentMaxNumber.text = viewModel.getMaxNumber().toString()
        binding.maxNumView.setBackgroundResource(background(viewModel.getMaxNumber()))
        binding.textScore.text = getStringScore(viewModel.getLastScore())
        binding.textRecord.text = getStringScore(viewModel.getRecord())

        val touchListener = MyTouchListener(requireContext())
        touchListener.setSideListener {
            when (it) {
                MySide.LEFT -> {
                    viewModel.swipeLeft()
                }
                MySide.RIGHT -> {
                    viewModel.swipeRight()
                }
                MySide.UP -> {
                    viewModel.swipeUp()
                }
                MySide.DOWN -> {
                    viewModel.swipeDown()
                }
            }
            if (viewModel.isWin()) {
                gameFinished("Congratulations!")
            }

            binding.textCurrentMaxNumber.text = viewModel.getMaxNumber().toString()
            binding.maxNumView.setBackgroundResource(background(viewModel.getMaxNumber()))
            binding.textScore.text = getStringScore(viewModel.getScore())
            binding.textRecord.text = getStringScore(viewModel.getRecord())
            if (viewModel.isNoWay()) {
                gameFinished()
            }
        }

        binding.group.setOnTouchListener(touchListener)

        binding.finishBtn.setOnClickListener { gameFinished() }

        binding.buttonNew.setOnClickListener {
            reloadGame()
        }

        viewModel.arrayLiveData.observe(viewLifecycleOwner, arrayObserver)
    }

    override fun onResume() {
        super.onResume()
        binding.buttonNew.visibility = View.VISIBLE
        binding.finishBtn.visibility = View.VISIBLE
    }

    private fun loadView() {
        for (i in 0 until binding.group.childCount) {
            val l = binding.group[i] as LinearLayoutCompat
            for (j in 0 until l.childCount) {
                views.add(l[j] as TextView)
            }
        }
    }

    private val arrayObserver = Observer<Array<Array<Int>>> {
        for (i in it.indices) {
            for (j in it[i].indices) {
                views[i * 5 + j].apply {
                    text = if (it[i][j] == 0) ""
                    else "${it[i][j]}"
                    setBackgroundResource(background(it[i][j]))
                }
            }
        }
    }

    private fun getStringScore(score: Long): String {
        var myScoreStr = "0"
        if (score < 1000) {
            myScoreStr = "$score".format(2)
        } else if (score in 1001..999999999) {
            myScoreStr = "${(score / 1000.0).format(2)}K"
        } else if (score > 1000_000_000) {
            myScoreStr = "${(score / 1000_000_000.0).format(2)}M"
        }
        return myScoreStr
    }

    private fun gameFinished(result_title: String = "Your Score") {
        binding.buttonNew.visibility = View.GONE
        binding.finishBtn.visibility = View.GONE
        var resultscreen = ResultScreen()
        val bundle = Bundle()
        bundle.putLong("MY_SCORE", viewModel.getScore())
        bundle.putString("RESULT_TITLE", result_title)
//        findNavController().navigate(R.id.action_mainScreen_to_resultScreen, bundle)
        resultscreen.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.screenMainContainer, resultscreen)
            .commit()
        reloadGame()
    }

    private fun reloadGame() {
        viewModel.reload()
        binding.textCurrentMaxNumber.text = viewModel.getMaxNumber().toString()
        binding.maxNumView.setBackgroundResource(background(viewModel.getMaxNumber()))
        binding.textScore.text = getStringScore(viewModel.getScore())
        binding.textRecord.text = getStringScore(viewModel.getRecord())
    }

    private fun playerStoppedGame() {
        viewModel.saveLastView()
    }

    override fun onPause() {
        super.onPause()
        playerStoppedGame()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}