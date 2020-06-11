package com.example.pruebacamerax

import com.example.pruebacamerax.entidades.UsuarioEntity

class GlobalUser {
    companion object{
        public var usuarioLogueado:UsuarioEntity = UsuarioEntity()
        public var datosReg:String = ""
        public var fileNameReg:String = ""
        public var direccionSelect:String = ""
    }
}