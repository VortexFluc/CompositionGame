package com.example.composition.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entity.GameResult

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_amount_cor_answers),
        count
    )
}

@BindingAdapter("requiredPercent")
fun bindRequiredPercent(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_percent_of_correct_answers),
        count
    )
}

@BindingAdapter("score")
fun bindScore(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.your_score),
        count
    )
}

@BindingAdapter("scorePercent")
fun bindPercent(textView: TextView, gameResult: GameResult) {
    textView.text = String.format(
        textView.context.getString(R.string.your_percent_of_correct_answers),
        getPercentOfRightAnswers(gameResult.countOfRightAnswers, gameResult.countOfQuestions)
    )
}

private fun getPercentOfRightAnswers(countAnswers: Int, countQuestions: Int): Int {
    if (countQuestions == 0) return 0
    return ((countAnswers / countQuestions.toDouble()) * 100).toInt()
}

@BindingAdapter("reactionImage")
fun bindImageReaction(imageView: ImageView, isWinner: Boolean) {
    imageView.setImageResource(getEmojiResId(isWinner))
}

private fun getEmojiResId(winner: Boolean): Int {
    if (winner) {
        return R.drawable.emoji_smile
    }
    return R.drawable.sad_face
}
