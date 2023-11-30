package com.example.reto.usuarioadapter

import android.graphics.Color
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.reto.R
import com.example.reto.databinding.ItemUsuarioBinding
import com.example.reto.fragments.AdminPanelFragmentDirections
import com.example.reto.fragments.HomeFragmentDirections
import com.example.reto.model.ChildDetails
import com.example.reto.model.Usuario

class UsuarioAdapter : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {



    class UsuarioViewHolder(val itemBinding: ItemUsuarioBinding):
            RecyclerView.ViewHolder(itemBinding.root)

    //it is used to update the contents according to the data base changes
    private val differCallback = object : DiffUtil.ItemCallback<ChildDetails>(){
        override fun areItemsTheSame(oldItem: ChildDetails, newItem: ChildDetails): Boolean {
            return oldItem.usuarioId == newItem.usuarioId &&
                    oldItem.nombre == newItem.nombre &&
                    oldItem.apellido == newItem.apellido &&
                    oldItem.edad == newItem.edad &&
                    oldItem.nivel_cognitivo == newItem.nivel_cognitivo &&
                    oldItem.nivel_motriz == newItem.nivel_motriz &&
                    oldItem.nivel_actual_minijuego == newItem.nivel_actual_minijuego
        }

        override fun areContentsTheSame(oldItem: ChildDetails, newItem: ChildDetails): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        return UsuarioViewHolder(
            ItemUsuarioBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val currentUser = differ.currentList[position]

        holder.itemBinding.nombreTextView.text = currentUser.nombre
        holder.itemBinding.apellidoTextView.text = currentUser.apellido

        holder.itemView.setOnClickListener{
            it.findNavController().navigate(R.id.action_loginFragment_to_nivelesFragment)
        }

        //val color = Color.argb(255, 127, 43, 58)
        //holder.itemBinding.root.setBackgroundColor(color)

        //holder.itemView.setOnClickListener{
            //val direction = LoginFragmentDirections.actionLoginFragmentToAdminPanelFragment2()
            //holder.itemView.findNavController().navigate(direction)
        //}
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }




}
