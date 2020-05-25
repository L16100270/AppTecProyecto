package com.example.pruebacamerax.entidades

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class UsuarioEntity {
    @PrimaryKey
    @ColumnInfo (name = "CORREO")
    var Correo:     String = ""

    @ColumnInfo (name = "PASSWORD")
    var Password:   String = ""

    @ColumnInfo (name = "NOMBRE")
    var Nombre:     String = ""

    @ColumnInfo (name = "APELLIDO")
    var Apellido:   String = ""

    @ColumnInfo (name = "TELEFONO")
    var Telefono:   String = ""

    @ColumnInfo (name = "VENDEDOR")
    var Vendedor:   Boolean = false

    @ColumnInfo (name = "IMAGEN")
    var ImagenPerfil: String = ""
}
