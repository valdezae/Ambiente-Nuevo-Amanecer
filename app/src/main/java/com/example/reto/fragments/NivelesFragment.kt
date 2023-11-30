package com.example.reto.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.reto.R
import com.example.reto.databinding.FragmentNivelesBinding


class NivelesFragment : Fragment() {

    private lateinit var binding: FragmentNivelesBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_niveles, container, false)



        binding.btnNivel1.setOnClickListener{
            it.findNavController().navigate(R.id.action_nivelesFragment_to_juego1Fragment)
        }

        binding.btnNivel2.setOnClickListener{
            it.findNavController().navigate(R.id.action_nivelesFragment_to_juego2Fragment)
        }
        binding.btnNivel3.setOnClickListener{
            it.findNavController().navigate(R.id.action_nivelesFragment_to_juego3Fragment)
        }

        binding.btnNivel4.setOnClickListener{
            it.findNavController().navigate(R.id.action_nivelesFragment_to_juego4Fragment)
        }

        return binding.root
    }


}