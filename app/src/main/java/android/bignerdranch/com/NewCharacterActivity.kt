package android.bignerdranch.com

import android.os.Bundle
import android.util.Log
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
            MainScope().launch {
                characterData = fetchCharacterData()
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
    }
}