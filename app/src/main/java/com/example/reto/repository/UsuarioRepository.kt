package com.example.reto.repository

import com.example.reto.localdatabase.UsuarioDatabase
import com.example.reto.model.ChildDetails
import com.example.reto.model.Usuario


//The repository is used to separate data retrieval and data usage
class UsuarioRepository(private val db: UsuarioDatabase) {

    suspend fun insertUsuario(usuario: Usuario) = db.getUsuarioDao().insertUsuario(usuario)
    suspend fun updateUsuario(usuario: Usuario) = db.getUsuarioDao().updateUsuario(usuario)
    suspend fun deleteUsuario(usuario: Usuario) = db.getUsuarioDao().deleteUsuario(usuario)

    fun getAllUsuarios() = db.getUsuarioDao().getAllUsuarios()
    fun searchUsuario(username: String, password: String) = db.getUsuarioDao().searchUsuarios(username, password)
    fun getUserType(username: String) = db.getUsuarioDao().getUserType(username)

    suspend fun insertChildDetails(childDetails: ChildDetails) = db.getUsuarioDao().insertChildDetails(childDetails)
    suspend fun updateChildDetails(childDetails: ChildDetails) = db.getUsuarioDao().updateChildDetails(childDetails)
    suspend fun deleteChildDetails(childDetails: ChildDetails) = db.getUsuarioDao().deleteChildDetails(childDetails)

    fun getAllChildDetails() = db.getUsuarioDao().getAllChildDetails()
}