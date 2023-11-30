package com.example.reto.localdatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.reto.model.ChildDetails
import com.example.reto.model.Usuario




//data access object (DAO)is a pattern that provides an abstract interface
// to some type of database or other persistence mechanism.
@Dao
interface UsuarioDAO {

    //Insert, update, and delete are coroutines (they are run on the background thread)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: Usuario)

    @Update
    suspend fun updateUsuario(usuario: Usuario)

    @Delete
    suspend fun deleteUsuario(usuario: Usuario)

    //Querys are run on the main thread, they don't need to be coroutines

    @Query("SELECT * FROM usuarios ORDER BY usuarioId DESC")
    fun getAllUsuarios(): LiveData<List<Usuario>>

    @Query("SELECT * FROM usuarios WHERE username LIKE :userName AND password LIKE :Password")
    fun searchUsuarios(userName: String, Password: String) : LiveData<List<Usuario>>

    @Query("SELECT userType FROM usuarios WHERE username = :username")
    fun getUserType(username: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChildDetails(childDetails: ChildDetails)

    @Update
    suspend fun updateChildDetails(childDetails: ChildDetails)

    @Delete
    suspend fun deleteChildDetails(childDetails: ChildDetails)

    @Query("SELECT * FROM child_details ORDER BY edad DESC")
    fun getAllChildDetails(): LiveData<List<ChildDetails>>
}