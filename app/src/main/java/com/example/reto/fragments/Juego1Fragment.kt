package com.example.reto.fragments

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.reto.R
import com.example.reto.databinding.FragmentJuego1Binding


class Juego1Fragment : Fragment() {

    private lateinit var binding: FragmentJuego1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_juego1,container, false)

        var mMediaPlayer : MediaPlayer = MediaPlayer.create(context, R.raw.dog3)
        mMediaPlayer.setOnCompletionListener { binding.imageViewJuego1.setImageResource(R.drawable.dog1) }
        binding.btnJuego1Regresar.setOnClickListener{
            it.findNavController().navigate(R.id.action_juego1Fragment_to_nivelesFragment)
        }

        binding.constraintlayout.setOnClickListener{
            binding.imageViewJuego1.setImageResource(R.drawable.dog2)
            mMediaPlayer!!.start()
        }

        return binding.root
    }


}