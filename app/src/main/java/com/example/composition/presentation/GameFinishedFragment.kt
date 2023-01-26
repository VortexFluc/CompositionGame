package com.example.composition.presentation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings

class GameFinishedFragment : Fragment() {

    private val args by navArgs<GameFinishedFragmentArgs>()

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    private val gameResult by lazy {
        args.gameResult
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
        findNavController().popBackStack()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}