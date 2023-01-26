package com.example.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.Question

interface OnOptionClickListener {
    fun onOptionClick(option: String)
}

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

@BindingAdapter("progressData")
fun bind_progressBar(progressBar: ProgressBar, progress: Int) {
    progressBar.setProgress(progress, true)
}

@BindingAdapter("question")
fun bind_question(textView: TextView, question: Question) {
    textView.text = question.sum.toString()
}

@BindingAdapter("changeAnswerColorIfTrue")
fun bind_enoughCount(textView: TextView, enough: Boolean) {
    textView.setTextColor(getColorByState(enough, textView.context))
}

@BindingAdapter("changePercentColorIfTrue")
fun bind_enoughCount(progressBar: ProgressBar, enough: Boolean) {
    val color = getColorByState(enough, progressBar.context)
    progressBar.progressTintList = ColorStateList.valueOf(color)
}

@BindingAdapter("addendum1")
fun bind_addendum1(textView: TextView, question: Question) {
    textView.text = question.visibleNumber.toString()
}

@BindingAdapter("secondaryData")
fun bind_secondaryProgress(progressBar: ProgressBar, progress: Int) {
    progressBar.secondaryProgress = progress
}

@BindingAdapter("onOptionClick")
fun bind_onOptionClick(textView: TextView, clickListener: OnOptionClickListener) {
    textView.setOnClickListener {
        clickListener.onOptionClick(textView.text.toString())
    }
}

private fun getColorByState(goodState: Boolean, context: Context): Int {
    val colorResId = if (goodState) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }
    val color = ContextCompat.getColor(context, colorResId)
    return color
}

