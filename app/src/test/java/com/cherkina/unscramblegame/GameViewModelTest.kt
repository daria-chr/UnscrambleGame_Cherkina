package com.cherkina.unscramblegame

import org.junit.Assert.*
import org.junit.Test
import com.cherkina.unscramblegame.data.MAX_NO_OF_WORDS
import com.cherkina.unscramblegame.data.SCORE_INCREASE
import com.cherkina.unscramblegame.ui_model.GameViewModel

class GameViewModelTest {
    private val viewModel= GameViewModel()
    private fun getUnscrambledWord(scrambleWord: String): String{
        return com.cherkina.unscramblegame.data.allWords.firstOrNull{
            word->scrambleWord.toSet()==word.toSet()
        }?: ""
    }

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset(){
        var currentGameUiState=viewModel.uiState.value
        val correctPlayerWord=getUnscrambledWord(currentGameUiState.currentScrambledWord)
        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()
        currentGameUiState=viewModel.uiState.value
        assertEquals(SCORE_INCREASE,currentGameUiState.score)
        assertFalse(currentGameUiState.isGuessedWordWrong)
    }

    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet(){
        val incorrectPlayerWord="incorrect"
        viewModel.updateUserGuess(incorrectPlayerWord)
        viewModel.checkUserGuess()
        val currentGameUiState=viewModel.uiState.value
        assertEquals(0,currentGameUiState.score)
        assertTrue(currentGameUiState.isGuessedWordWrong)
    }
    @Test
    fun gameViewModel_Initialization_FirstWordLoaded(){
        val gameUiState=viewModel.uiState.value
        val unscrambledWord=getUnscrambledWord(gameUiState.currentScrambledWord)
        assertNotEquals(unscrambledWord, gameUiState.currentScrambledWord)
        assertTrue(gameUiState.currentWordCount == 1)
        assertTrue(gameUiState.score == 0)
        assertFalse(gameUiState.isGameOver)
    }
    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly(){
        var expectedScore=0
        var currentGameUiState=viewModel.uiState.value
        var correctPlayerWord=getUnscrambledWord(currentGameUiState.currentScrambledWord)
        repeat(MAX_NO_OF_WORDS){
            expectedScore += SCORE_INCREASE
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
            currentGameUiState = viewModel.uiState.value
            correctPlayerWord = getUnscrambledWord(currentGameUiState
                .currentScrambledWord)
            assertEquals(expectedScore, currentGameUiState.score)
        }
        assertEquals(MAX_NO_OF_WORDS,currentGameUiState.currentWordCount)
        assertTrue(currentGameUiState.isGameOver)

    }
}