package com.example.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.composition.R
import com.example.composition.data.GameRepository_Impl
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecases.GenerateQuestionUseCase
import com.example.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }

    private val generateQuestionUseCase: GenerateQuestionUseCase =
        GenerateQuestionUseCase(GameRepository_Impl)
    private val getGameSettingsUseCase: GetGameSettingsUseCase =
        GetGameSettingsUseCase(GameRepository_Impl)
    private val context = application

    private lateinit var _gameSettings: GameSettings
    private lateinit var level: Level
    private var timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private var _countOfRightAnswers = 0
    private var _countOfQuestions = 0

    private var _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _progressString = MutableLiveData<String>()
    val progressString: LiveData<String>
        get() = _progressString

    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCount

    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult


    fun setupGame(level: Level) {
        getGameSettings(level)
        setupTimer()
        generateQuestion()
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        _gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = _gameSettings.minPercentOfRightAnswers
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(_gameSettings.maxSumValue)
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentOfRightAnswers.value = percent
        _progressString.value = String.format(
            context.resources.getString(R.string.correct_answers_minimum),
            _countOfRightAnswers,
            _gameSettings.minCountOfRightAnswers
        )
        _enoughCount.value =
            _countOfRightAnswers >= _gameSettings.minCountOfRightAnswers
        _enoughPercent.value = percent >= _gameSettings.minPercentOfRightAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int {
        return ((_countOfRightAnswers / _countOfQuestions.toDouble()) * 100).toInt()
    }

    fun answer(answer: String) {
        question.value?.let {
            checkAnswer(answer, it)
            generateQuestion()
            updateProgress()
        }
    }

    private fun checkAnswer(answer: String, it: Question) {
        val answerInt = answer.toInt()
        val correctAnswer = it.rightAnswer

        if (answerInt == correctAnswer) {
            _countOfRightAnswers++
        }
        _countOfQuestions++
    }

    private fun setupTimer() {
        timer =
            object : CountDownTimer(
                _gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS,
                MILLIS_IN_SECONDS
            ) {
                override fun onTick(millisUntilFinished: Long) {
                    _formattedTime.value = formatTime(millisUntilFinished)
                }

                override fun onFinish() {
                    finishGame()
                }
            }
        timer?.start()
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)

        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            enoughCount.value == true && enoughPercent.value == true,
            _countOfRightAnswers,
            _countOfQuestions,
            _gameSettings
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

}