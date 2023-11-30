package com.example.reto.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "usuarios")
@Parcelize
data class Usuario(
    @PrimaryKey(autoGenerate = true) val usuarioId: Long,
    val username: String,
    val password: String,
    val userType: UserType
):Parcelable

enum class UserType{
    ADMIN,
    PARENT,
    CHILD
}
