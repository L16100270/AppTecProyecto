package com.example.pruebacamerax.dao


import androidx.room.*
import com.example.pruebacamerax.entidades.UsuarioEntity

@Dao
interface UsuarioDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun InsertUsuario(usuario: UsuarioEntity)

    @Query("SELECT * FROM UsuarioEntity WHERE CORREO = :search ")
    fun getUsuario(search: String) : List<UsuarioEntity>

    @Query("SELECT * FROM UsuarioEntity")
    fun getAllUsuario(): List<UsuarioEntity>

    @Update
    fun updateUsuario(usuario: UsuarioEntity)
}