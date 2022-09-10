package com.example.simplewordle

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private var guessCounter = 0
    private var correctnessStr = ""

    // Call helper class FourLetterWordList.kt to generate random 4 letter word
    val wordToGuess = FourLetterWordList.getRandomFourLetterWord()
    private var guessWord = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val guessBtn = findViewById<Button>(R.id.guessBtn)
        val resetBtn = findViewById<Button>(R.id.resetBtn)

        val wordReveal = findViewById<TextView>(R.id.randomWordTextView)

        val userInputField = findViewById<TextInputEditText>(R.id.userInputField)
        var firstWord = findViewById<TextView>(R.id.firstGuess)
        var guess1text = findViewById<TextView>(R.id.guess1text)
        var guess1checkText = findViewById<TextView>(R.id.guess1check)
        val correctness1 = findViewById<TextView>(R.id.correctness1)

        var secondWord = findViewById<TextView>(R.id.secondGuess)
        var guess2text = findViewById<TextView>(R.id.guess2text)
        var guess2checkText = findViewById<TextView>(R.id.guess2check)
        val correctness2 = findViewById<TextView>(R.id.correctness2)

        var thirdWord = findViewById<TextView>(R.id.thirdGuess)
        var guess3text = findViewById<TextView>(R.id.guess3text)
        var guess3checkText = findViewById<TextView>(R.id.guess3check)
        val correctness3 = findViewById<TextView>(R.id.correctness3)


        // Used to Display random word to screen
        wordReveal.text = wordToGuess

        guessBtn.setOnClickListener {
            if (guessCounter == 0) {
                // Save user input, clear text field and close keyboard
                saveInputAndCloseKeyboard(firstWord, userInputField)

                // Show user's first guess
                displayUsersGuess(firstWord, guess1text)

                // Check user's first word for correctness
                checkAndDisplayCorrectness(firstWord, guess1checkText, correctness1)

                // If user gets the word right on the first try, reveal reset button and word
                if (firstWord.text == wordToGuess) {
                    guessBtn.isEnabled = false
                    guessBtn.isClickable = false
                    wordReveal!!.visibility = View.VISIBLE
                    resetBtn.visibility = View.VISIBLE
                }
            }
            else if (guessCounter == 1) {
                // Save user input, display on screen, and clear text field
                saveInputAndCloseKeyboard(secondWord, userInputField)

                // Show user's second guess
                displayUsersGuess(secondWord, guess2text)

                // Check user's second word for correctness
                checkAndDisplayCorrectness(secondWord, guess2checkText, correctness2)

                if (secondWord.text == wordToGuess) {
                    guessBtn.isEnabled = false
                    guessBtn.isClickable = false
                    wordReveal!!.visibility = View.VISIBLE
                    resetBtn.visibility = View.VISIBLE
                }

            }
            else {
                // Save user input, clear text field and close keyboard
                saveInputAndCloseKeyboard(thirdWord, userInputField)

                // Show user's third guess
                displayUsersGuess(thirdWord, guess3text)

                // Check user's third word for correctness
                checkAndDisplayCorrectness(thirdWord, guess3checkText, correctness3)

                if (thirdWord.text == wordToGuess) {
                    guessBtn.isEnabled = false
                    guessBtn.isClickable = false
                    wordReveal!!.visibility = View.VISIBLE
                    resetBtn.visibility = View.VISIBLE
                }
            }

            // Once user reaches guess limit OR guesses answer right, disable button and reveal answer
            if (guessCounter == 3) {
                guessBtn.isEnabled = false
                guessBtn.isClickable = false
                wordReveal.visibility = View.VISIBLE
                resetBtn.visibility = View.VISIBLE
            }
        }
        resetBtn.setOnClickListener {
            // reset counter
            guessCounter = 0

            // Enable buttons and hide word and reset button
            guessBtn.isEnabled = true
            guessBtn.isClickable = true
            wordReveal.visibility = View.INVISIBLE
            resetBtn.visibility = View.INVISIBLE

            // Clear the TextViews for each word
            firstWord.text = ""
            secondWord.text = ""
            thirdWord.text = ""

            // Set TextViews to invisible
            firstWord.visibility = View.INVISIBLE
            secondWord.visibility = View.INVISIBLE
            thirdWord.visibility = View.INVISIBLE
            guess1checkText.visibility = View.INVISIBLE
            guess2checkText.visibility = View.INVISIBLE
            guess3checkText.visibility = View.INVISIBLE
            guess1text.visibility = View.INVISIBLE
            guess2text.visibility = View.INVISIBLE
            guess3text.visibility = View.INVISIBLE
            correctness1.visibility = View.INVISIBLE
            correctness2.visibility = View.INVISIBLE
            correctness3.visibility = View.INVISIBLE
        }
    }

    private fun checkAndDisplayCorrectness(word: TextView?, textToDisplay: TextView?, correctness: TextView?) {
        correctnessStr = checkGuess(guessWord, wordToGuess)
        guessCounter++
        if (correctness != null) {
            correctness.text = correctnessStr
            correctness.visibility = View.VISIBLE
        }
        if (textToDisplay != null) {
            textToDisplay.visibility = View.VISIBLE
        }
    }

    private fun displayUsersGuess(wordToDisplay: TextView?, title: TextView?) {
        if (wordToDisplay != null) {
            wordToDisplay.visibility = View.VISIBLE
        }
        if (title != null) {
            title.visibility = View.VISIBLE
        }
    }


    private fun saveInputAndCloseKeyboard(guessInput: TextView, userInputField: TextInputEditText) {
        guessWord = userInputField.text.toString().uppercase()
        guessInput.text = guessWord
        userInputField.text?.clear()
        closeKeyboard()
        userInputField.requestFocus()
    }


    /**
     * Parameters / Fields:
     *   wordToGuess : String - the target word the user is trying to guess
     *   guess : String - what the user entered as their guess
     *
     * Returns a String of 'O', '+', and 'X', where:
     *   'O' represents the right letter in the right place
     *   '+' represents the right letter in the wrong place
     *   'X' represents a letter not in the target word
     */
    private fun checkGuess(guess: String, wordToGuess: String) : String {
        var result = ""
        for (i in 0..3) {
            if (guess[i] == wordToGuess[i]) {
                result += "O"
            }
            else if (guess[i] in wordToGuess) {
                result += "+"
            }
            else {
                result += "X"
            }
        }
        return result
    }

// extension function to hide soft keyboard programmatically
fun Activity.closeKeyboard(){
    (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}

}