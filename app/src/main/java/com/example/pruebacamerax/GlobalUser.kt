package com.example.pruebacamerax

import com.example.pruebacamerax.entidades.UsuarioEntity

class GlobalUser {
    companion object{
        public var usuarioLogueado:UsuarioEntity = UsuarioEntity()
    }
}