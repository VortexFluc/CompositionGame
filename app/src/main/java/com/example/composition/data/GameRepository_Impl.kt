package com.example.composition.data

import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Level.*
import com.example.composition.domain.entity.Question
import com.example.composition.domain.repository.GameRepository
import java.lang.Integer.max
import java.lang.StrictMath.min
import kotlin.random.Random

object GameRepository_Impl : GameRepository {

    private const val MIN_SUM_VALUE = 2
    private const val MIN_VISIBLE_NUMBER = 1

    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue + 1)
        val visibleNumber = Random.nextInt(MIN_VISIBLE_NUMBER, sum)
        val options = HashSet<Int>()
        val correctAnswer = sum - visibleNumber

        options.add(correctAnswer)

        val from = max(correctAnswer - countOfOptions, MIN_VISIBLE_NUMBER)
        val to = min(maxSumValue, correctAnswer + countOfOptions)

        while (options.size < countOfOptions) {
            options.add(Random.nextInt(from, to))
        }

        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {
            TEST -> GameSettings(
                10,
                3,
                50,
                8
            )
            EASY -> GameSettings(
                15,
                3,
                50,
                60
            )
            NORMAL -> GameSettings(
                20,
                20,
                80,
                40
            )
            HARD -> GameSettings(
                30,
                30,
                90,
                40
            )
        }
    }
}