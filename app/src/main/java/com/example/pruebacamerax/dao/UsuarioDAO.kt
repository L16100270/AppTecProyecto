package com.example.pruebacamerax.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pruebacamerax.entidades.UsuarioEntity

@Dao
interface UsuarioDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun InsertUsuario(usuario: UsuarioEntity)

    @Query("SELECT * FROM UsuarioEntity WHERE CORREO = :search ")
    fun getUsuario(search: String) : List<UsuarioEntity>

    @Query("SELECT * FROM UsuarioEntity")
    fun getAllUsuario(): List<UsuarioEntity>
}