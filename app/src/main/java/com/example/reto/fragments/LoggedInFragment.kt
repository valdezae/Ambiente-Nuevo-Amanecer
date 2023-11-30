package com.example.reto.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.reto.FirestoreManager
import com.example.reto.R
import com.example.reto.databinding.FragmentLoggedInBinding
import com.example.reto.databinding.FragmentLoginBinding
import com.example.reto.localdatabase.UsuarioDatabase
import com.example.reto.model.ChildDetails
import com.example.reto.repository.UsuarioRepository
import com.example.reto.usuarioadapter.UsuarioAdapter
import com.example.reto.viewmodel.UsuarioViewModel
import com.example.reto.viewmodel.UsuarioViewModelFactory

class LoggedInFragment : Fragment() {

    private lateinit var binding: FragmentLoggedInBinding
    private val usuarioAdapter = UsuarioAdapter()
    private lateinit var usuarioViewModel : UsuarioViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_logged_in, container, false)

        //var input = requireArguments().getString("name")
        //binding.textViewLoginFrag.text = input.toString()

        binding.userRecyclerView.apply {
            adapter = usuarioAdapter
        }



        //binding.btnAlex.setOnClickListener{
            //it.findNavController().navigate(R.id.action_loginFragment_to_nivelesFragment)
        //}

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userType = arguments?.getString("userType")
        val bundle = Bundle().apply {
            putString("userType", userType)
        }

        binding.btnLogin.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_adminPanelFragment2, bundle)
        }

        // Fetch and display child users from Firestore
        FirestoreManager.firestore.collection("alumnos")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val childUsers = mutableListOf<ChildDetails>()
                for (document in querySnapshot) {
                    val childUser = document.toObject(ChildDetails::class.java)
                    childUsers.add(childUser)
                }
                usuarioAdapter.differ.submitList(childUsers)
            }
            .addOnFailureListener { exception ->
                // Handle failures in fetching data
                // Log the exception or show an error message
                Toast.makeText(
                    context,
                    "Failed to fetch child users: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}