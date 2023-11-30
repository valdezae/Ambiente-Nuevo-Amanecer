package com.example.reto.usuarioadapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.reto.R
import com.example.reto.databinding.ItemUsuarioBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class AlumnoUpdateAdapter : RecyclerView.Adapter<AlumnoUpdateAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(val itemBinding: ItemUsuarioBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<DocumentSnapshot>() {
        override fun areItemsTheSame(oldItem: DocumentSnapshot, newItem: DocumentSnapshot): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DocumentSnapshot, newItem: DocumentSnapshot): Boolean {
            // You may need to customize this based on your data structure
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        return UsuarioViewHolder(
            ItemUsuarioBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val currentUser = differ.currentList[position]

        holder.itemBinding.nombreTextView.text = currentUser.getString("nombre")
        holder.itemBinding.apellidoTextView.text = currentUser.getString("apellido")

        holder.itemView.setOnClickListener{
            val childId = currentUser.id
            val bundle = Bundle().apply {
                putString("childId",childId)
            }
            it.findNavController().navigate(R.id.action_updateAlumnoFragment_to_alumnoDetailsUpdateFragment,bundle)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
