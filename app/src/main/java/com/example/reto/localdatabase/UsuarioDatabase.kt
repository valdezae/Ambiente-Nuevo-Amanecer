package com.example.reto.localdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.reto.model.ChildDetails
import com.example.reto.model.Usuario

@Database(entities = [Usuario::class, ChildDetails::class], version = 2)
abstract class UsuarioDatabase: RoomDatabase() {

    abstract fun getUsuarioDao(): UsuarioDAO

    //Companion objects are a way to implement the singleton pattern in android studio.
    companion object{
        //the volatile annotation is used to notify other threads about any changes to this thread.
        @Volatile
        private var instance: UsuarioDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?:
        //synchronized is used when dealing with multithreading
        // and helps prevent any unsynchronized data between the threads
            synchronized(LOCK){
                instance ?:
                    createDatabase(context).also{
                        instance = it
                    }
            }

        private fun createDatabase(context: Context)=
            Room.databaseBuilder(
                context.applicationContext,
                UsuarioDatabase::class.java,
                "usuario_db"
            ).build()
    }
}