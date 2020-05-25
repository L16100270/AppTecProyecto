package com.example.pruebacamerax

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pruebacamerax.GlobalUser.Companion.usuarioLogueado
import kotlinx.android.synthetic.main.activity_cuenta.*

class CuentaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuenta)
        cargarDatos()
    }

    fun cargarDatos(){
        ivFotoPerfil.setImageURI(Uri.parse(usuarioLogueado.ImagenPerfil))
        tvNombreCuenta.text     = usuarioLogueado.Nombre + " " + usuarioLogueado.Apellido
        txtCorreoCuenta.text    = usuarioLogueado.Correo
        etTelefonoCuenta.setText(usuarioLogueado.Telefono)
    }
}
