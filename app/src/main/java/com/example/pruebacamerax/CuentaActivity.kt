package com.example.pruebacamerax

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pruebacamerax.GlobalUser.Companion.usuarioLogueado
import com.example.pruebacamerax.fragments.CameraFragment.Companion.URI_PROFILE
import com.example.pruebacamerax.roomdb.DBAppTec
import kotlinx.android.synthetic.main.activity_cuenta.*

class CuentaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuenta)
        cargarDatos()
        URI_PROFILE = Uri.parse(usuarioLogueado.ImagenPerfil)
        ivFotoPerfilU.setOnClickListener {
            AbrirCam()
        }
        btnActualizaU.setOnClickListener {
            actualizar()
        }
    }

    fun AbrirCam(){
        val intentAct = Intent(this,FotoPerfilActivity::class.java)
        startActivity(intentAct)
    }

    fun cargarDatos(){
        ivFotoPerfilU.setImageURI(Uri.parse(usuarioLogueado.ImagenPerfil))
        etCorreoU.text = usuarioLogueado.Correo
        etNombreU.setText(usuarioLogueado.Nombre)
        etApellidosU.setText(usuarioLogueado.Apellido)
        etContraU.setText(usuarioLogueado.Password)
        etTelefonoU.setText(usuarioLogueado.Telefono)
    }

    fun actualizar(){
        if (etNombreU.text.trim().isNullOrEmpty()) {
            Toast.makeText(this,"Ingrese Nombre", Toast.LENGTH_SHORT).show()
            return
        }
        if (etApellidosU.text.trim().isNullOrEmpty()) {
            Toast.makeText(this,"Ingrese Apellidos", Toast.LENGTH_SHORT).show()
            return
        }
        if (etContraU.text.trim().isNullOrEmpty()) {
            Toast.makeText(this,"Ingrese Contraseña", Toast.LENGTH_SHORT).show()
            return
        }
        if (etTelefonoU.text.trim().isNullOrEmpty()) {
            Toast.makeText(this,"Ingrese Telefono", Toast.LENGTH_SHORT).show()
            return
        }
        if(URI_PROFILE == null)
        {
            Toast.makeText(this,"Capture una Foto de Perfil", Toast.LENGTH_SHORT).show()
            return
        }

        usuarioLogueado.Nombre = etNombreU.text.trim().toString()
        usuarioLogueado.Apellido = etApellidosU.text.trim().toString()
        usuarioLogueado.Telefono = etTelefonoU.text.trim().toString()
        usuarioLogueado.Password = etContraU.text.toString()
        usuarioLogueado.ImagenPerfil = URI_PROFILE.toString()

        var HiloUpdate = object:Thread(){
            override fun run() {
                try {
                    DBAppTec.get(application).getUsuarioDAO().updateUsuario(usuarioLogueado)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        HiloUpdate.start()
        HiloUpdate.join()
        Toast.makeText(this,"Cuenta Actualizada", Toast.LENGTH_SHORT).show()
    }
    override fun onResume() {
        super.onResume()
        if(URI_PROFILE != null) {
            ivFotoPerfilU.setImageURI(URI_PROFILE)
        }
    }
}
