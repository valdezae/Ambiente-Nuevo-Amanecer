package com.example.reto.fragments

import android.os.Bundle
import android.speech.tts.TextToSpeech.OnInitListener
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.reto.R
import com.example.reto.databinding.FragmentJuego4Binding
import java.util.Locale

class Juego4Fragment : Fragment() {

    private lateinit var binding: FragmentJuego4Binding
    private var tts: TextToSpeech? = null
    private var buildWord: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_juego4,container, false)

        binding.btnJuego4Regresar.setOnClickListener{
            it  .findNavController().navigate(R.id.action_juego4Fragment_to_nivelesFragment)
        }

        var imagea = arrayOf<Int>(R.drawable.juego_1, R.drawable.juego_1, R.drawable.juego_1, R.drawable.juego_1, R.drawable.juego_1, R.drawable.juego_1, R.drawable.juego_1, R.drawable.juego_1, R.drawable.juego_1, R.drawable.juego_1)
        var texta = arrayOf<String>("veterinario", "animales", "burro", "patito", "iguana", "pulpo", "cucaracha", "delfín", "pez", "cabra")

        val ttsOnInitListener = OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val locale = Locale("es", "MX")
                if (tts!!.isLanguageAvailable(locale) == TextToSpeech.LANG_AVAILABLE) {
                    tts!!.setLanguage(locale)
                } else {
                    Log.e("TTS", "The desired language is not supported.")
                }
            } else {
                Log.e("TTS", "Initialization failed.")
            }
        }

        tts = TextToSpeech(activity, ttsOnInitListener)

        for((index, item) in imagea.withIndex()) {
            val newImageButton = ImageButton(activity)
            newImageButton.id = index
            newImageButton.setBackgroundResource(item)
            newImageButton.tooltipText = texta[index]
            newImageButton.setOnClickListener(View.OnClickListener { speakOut(newImageButton.tooltipText.toString())
                buildWord = buildWord + newImageButton.tooltipText.toString() + " "
                var phraseButton = ImageButton(activity)
                phraseButton.tooltipText = newImageButton.tooltipText
                phraseButton.background = newImageButton.background
                phraseButton.isActivated = false
                showPhrase(phraseButton)
                //buildWord()


            })
            binding.gridLayoutButtons.addView(newImageButton, 850, 850)
        }

        binding.buttonPhrase.isActivated = buildWord.isNotEmpty()
        binding.buttonPhrase.setOnClickListener {
            speakOut(buildWord)
        }

        binding.buttonClear.isActivated =buildWord.isNotEmpty()
        binding.buttonClear.setOnClickListener{
            buildWord = ""
            binding.gridLayoutPhrase.removeAllViews()
        }


        return binding.root
    }

    private fun showPhrase(newImageButton: ImageButton) {
        binding.gridLayoutPhrase.addView(newImageButton, 350, 350)
    }

    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }



}