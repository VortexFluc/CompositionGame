package com.example.composition.presentation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings

class GameFinishedFragment : Fragment() {

    companion object {
        private const val ARG_RESULT = "result"
        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_RESULT, gameResult)
                }
            }
        }
    }


    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    private lateinit var gameResult: GameResult


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    private fun parseArgs() {
        requireArguments().getParcelable<GameResult>(ARG_RESULT)?.let {
            gameResult = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    retryGame()
                }
            })

        with(binding) {
            button.setOnClickListener {
                retryGame()
            }
        }

        setupViews()

    }

    private fun setupViews() {
        with(binding) {

            ivReaction.setImageResource(getEmojiResId())

            tvMinCorrectAnswers.text = String.format(
                getString(R.string.required_amount_cor_answers),
                gameResult.gameSettings.minCountOfRightAnswers
            )

            tvScore.text = String.format(
                getString(R.string.your_score),
                gameResult.countOfRightAnswers
            )

            tvMinPercent.text = String.format(
                getString(R.string.required_percent_of_correct_answers),
                gameResult.gameSettings.minPercentOfRightAnswers
            )

            tvPercent.text = String.format(
                getString(R.string.your_percent_of_correct_answers),
                getPercentOfRightAnswers()
            )
        }
    }

    private fun getPercentOfRightAnswers(): Int {
        if (gameResult.countOfQuestions == 0) return 0
        return ((gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble()) * 100).toInt()
    }

    private fun getEmojiResId(): Int {
        if (gameResult.winner) {
            return R.drawable.emoji_smile
        }
        return R.drawable.sad_face
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}