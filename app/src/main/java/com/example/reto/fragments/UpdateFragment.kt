package com.example.reto.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.reto.FirestoreManager
import com.example.reto.R
import com.example.reto.databinding.FragmentAdminPanelBinding
import com.example.reto.databinding.FragmentLoggedInBinding
import com.example.reto.databinding.FragmentUpdateBinding
import com.example.reto.localdatabase.UsuarioDatabase
import com.example.reto.model.Usuario
import com.example.reto.repository.UsuarioRepository
import com.example.reto.usuarioadapter.UsuarioAdapter
import com.example.reto.usuarioadapter.UsuarioAdapter2
import com.example.reto.viewmodel.UsuarioViewModel
import com.example.reto.viewmodel.UsuarioViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateBinding
    private val usuarioAdapter2 = UsuarioAdapter2()
    private lateinit var usuarioViewModel : UsuarioViewModel
    private lateinit var mView : View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update, container, false)

        val userTypes = arrayOf("ADMIN", "PARENT")

// Initialize the spinner
        val spinner: Spinner = binding.spinnerUserTypeUpdate
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, userTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.prompt = "Selecciona un tipo de usuario"
        spinner.adapter = adapter


// Handle item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedUserType = userTypes[position]
                // Handle the selected user type
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing here
            }
        }

        binding.btnUpdateRegresar.setOnClickListener{
            it.findNavController().navigate(R.id.action_updateFragment_to_adminPanelFragment2)
        }
        binding.btnUpdate.setOnClickListener{
            updateCuentaAuth()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usuarioDatabase = UsuarioDatabase.invoke(requireContext().applicationContext)
        val usuarioRepository = UsuarioRepository(usuarioDatabase)
        usuarioViewModel = ViewModelProvider(this, UsuarioViewModelFactory(requireActivity().application, usuarioRepository)).get(UsuarioViewModel::class.java)
        mView = view

    }



    private fun updateCuentaAuth() {
        val userName = binding.editTextUserNameUpdate.text.toString().trim()
        val userPassword = binding.editTextPasswordUpdate.text.toString().trim()
        val userTypeString = binding.spinnerUserTypeUpdate.selectedItem.toString()
        val user = FirebaseAuth.getInstance().currentUser

        // Validate user input (you may want to add more validation)
        if (userName.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(requireContext(), "Username and password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Update email
        user?.verifyBeforeUpdateEmail("$userName@nuevoAmanecer.com")
            ?.addOnCompleteListener { emailUpdateTask ->
                if (emailUpdateTask.isSuccessful) {
                    Toast.makeText(requireContext(), "Username updated successfully", Toast.LENGTH_SHORT).show()

                    // Update password
                    user.updatePassword(userPassword)
                        .addOnCompleteListener { passwordUpdateTask ->
                            if (passwordUpdateTask.isSuccessful) {
                                Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_SHORT).show()

                                // Update Firestore
                                updateCuentaFirestore(user.uid, userName, userPassword, userTypeString)
                            } else {
                                Toast.makeText(requireContext(), "Failed to update password", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(requireContext(), "Failed to update username", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateCuentaFirestore(userId: String, userName: String, userPassword: String, userType: String) {
        val userRef = FirebaseFirestore.getInstance().collection("usuarios").document(userId)

        val updates = mapOf(
            "userName" to userName,
            "userPassword" to userPassword,
            "userType" to userType
        )

        userRef.update(updates)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Data updated successfully", Toast.LENGTH_SHORT).show()
                mView.findNavController().navigate(R.id.action_updateFragment_to_adminPanelFragment2)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to update data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }



}