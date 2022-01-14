package android.bignerdranch.com

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_new_character.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private const val CHARACTER_DATA_KEY = "CHARACTER_DATA_KEY"
private var Bundle.characterData
    get() = getSerializable(CHARACTER_DATA_KEY) as CharacterGenerator.CharacterData
    set(value) = putSerializable(CHARACTER_DATA_KEY, value)

class NewCharacterActivity : AppCompatActivity() {
    private var characterData = CharacterGenerator.generate()
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.characterData = characterData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_character)
        characterData = savedInstanceState?.characterData ?: CharacterGenerator.generate()
        generateButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            generateButton.text = ""
            MainScope().launch {
                try { characterData = fetchCharacterData() }
                catch(e: Exception) {
                    Log.e("Caught", "Caught: $e", e)
                    Toast.makeText(
                        this@NewCharacterActivity,
                        R.string.error_message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Log.d("Networking", "Character generated")
                displayCharacterData()
            }
        }
        quitButton.setOnClickListener {
            this.finish()
        }
        displayCharacterData()
    }

    private fun displayCharacterData() {
        characterData.run {
            nameTextView.text = name
            raceTextView.text = race
            dexterityTextView.text = dex
            wisdomTextView.text = wis
            strengthTextView.text = str
        }
        progressBar.visibility = View.GONE
        generateButton.text = getString(R.string.generate)
    }
}