package com.example.reto.model


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "child_details")
data class ChildDetails(
    @PrimaryKey(autoGenerate = true)
    val usuarioId: Long,
    val nombre: String,
    val apellido: String,
    val edad: Int,
    val nivel_cognitivo: Int,
    val nivel_motriz: Int,
    val nivel_actual_minijuego: Int
): Parcelable {
    // Add a no-argument constructor
    // This is needed for Firestore deserialization
    constructor() : this(
        usuarioId = 0,
        nombre = "",
        apellido = "",
        edad = 0,
        nivel_cognitivo = 0,
        nivel_motriz = 0,
        nivel_actual_minijuego = 0
    )
}
