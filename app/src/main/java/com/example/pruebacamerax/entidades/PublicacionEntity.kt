package com.example.pruebacamerax.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class PublicacionEntity {
    @PrimaryKey
    @ColumnInfo(name = "DIRECCION")
    var Direccion: String = ""

    @ColumnInfo(name = "HABITACIONES")
    var Habitaciones: Int = 0

    @ColumnInfo(name = "BANOS")
    var Banos: Int = 0

    @ColumnInfo(name = "TIENECOCINA")
    var tieneCocina: Boolean = false

    @ColumnInfo(name = "TIENECOMEDOR")
    var tieneComedor: Boolean = false

    @ColumnInfo(name="PRECIO")
    var Precio: Double = 0.0

    @ColumnInfo(name = "VENDIDA")
    var Vendida: Boolean = false

    @ColumnInfo(name = "IMAGENCASA")
    var ImagenCasa: String = ""

    @ColumnInfo(name = "CONTACTO")
    var Contacto: String = ""
}