package com.example.pruebacamerax.dao

import androidx.room.*
import com.example.pruebacamerax.entidades.PublicacionEntity

@Dao
interface PublicacionDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun InsertPublicacion(publicacion: PublicacionEntity)

    @Query("SELECT * FROM PublicacionEntity")
    fun getAllPublicacion(): List<PublicacionEntity>

    @Query("SELECT * FROM PublicacionEntity WHERE DIRECCION = :search ")
    fun getPublicacion(search: String) : List<PublicacionEntity>

    @Update
    fun updatePublicacion(publicacion: PublicacionEntity)
}