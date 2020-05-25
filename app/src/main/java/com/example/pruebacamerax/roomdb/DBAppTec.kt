package com.example.pruebacamerax.roomdb

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pruebacamerax.dao.UsuarioDAO
import com.example.pruebacamerax.entidades.UsuarioEntity

@Database(version = 1, entities = [UsuarioEntity::class])
abstract class DBAppTec: RoomDatabase() {
    companion object {
        fun get(application: Application):DBAppTec {
            return Room.databaseBuilder(application, DBAppTec::class.java, "AirTec2").build()
        }
    }
    abstract fun getUsuarioDAO(): UsuarioDAO
}