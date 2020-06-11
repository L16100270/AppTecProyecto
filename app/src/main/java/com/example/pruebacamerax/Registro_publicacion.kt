package com.example.pruebacamerax

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.pruebacamerax.GlobalUser.Companion.usuarioLogueado
import com.example.pruebacamerax.entidades.PublicacionEntity
import com.example.pruebacamerax.fragments.CameraFragment
import com.example.pruebacamerax.fragments.CameraFragment.Companion.URI_PROFILE
import com.example.pruebacamerax.roomdb.DBAppTec
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_registro_publicacion.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Registro_publicacion : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_publicacion)
        URI_PROFILE = null
        ivCasaProfile.setOnClickListener{
            AbrirCamara()
        }
        btnRegistrar.setOnClickListener {
            Registrar()
        }
    }

    fun AbrirCamara()
    {
        val intentAct = Intent(this,FotoPerfilActivity::class.java)
        startActivity(intentAct)
    }

    fun Registrar(){
        var existe:Boolean = false
        var cantidad:Int = 0
        Log.d("llego", "paso este punto 1")
        if (etDireccion.text.trim().isNullOrEmpty()){
            Toast.makeText(this,"Ingrese Direccion", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("llego", "paso este punto 2" )
        val Hilo = object:Thread(){
            override fun run() {
                try {
                    existe = DBAppTec.get(application).getPublicacionDAO().
                    getPublicacion(etDireccion.text.trim().toString()).size > 0
                }catch(e: Exception)
                {
                    e.printStackTrace()
                }
            }
        }
        Hilo.start()
        Hilo.join()
        Log.d("llego", "paso este punto3 ")
        if(existe){
            Toast.makeText(this,"Esta direccion ya fue publicada", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("llego", "paso este punto4 ")
        if(URI_PROFILE == null) {
            Toast.makeText(this,"Capture una Foto de la Vivienda", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("llego", "paso este punto5 ")
        if(etHabitaciones.text.trim().isNullOrEmpty()){
            Toast.makeText(this,"Ingrese un numero de Habitaciones", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("llego", "paso este punto6 ")
        if(etBanos.text.trim().isNullOrEmpty()){
            Toast.makeText(this,"Ingrese un numero de Baños", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("llego", "paso este punto7 ")
        if(etPrecio.text.trim().isNullOrEmpty()){
            Toast.makeText(this,"Ingrese un precio para el inmueble", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("llego", "paso este punto8 ")
        var publicacionNuevo = PublicacionEntity()
        Log.d("llego", "paso este punto9 ")
        publicacionNuevo.Direccion      = etDireccion.text.trim().toString()
        Log.d("llego", "paso este punto10 ")
        publicacionNuevo.Banos          = etBanos.text.toString().toInt()
        Log.d("llego", "paso este punto11 ")
        publicacionNuevo.Habitaciones   = etHabitaciones.text.toString().toInt()
        Log.d("llego", "paso este punto12 ")
        publicacionNuevo.ImagenCasa     = URI_PROFILE.toString()
        Log.d("llego", "paso este punto13 ")
        publicacionNuevo.Vendida        = false
        Log.d("llego", "paso este punto14 ")
        publicacionNuevo.Contacto = usuarioLogueado.Telefono
        try{
            publicacionNuevo.Precio         = etPrecio.text.toString().toDouble()
        }
        catch(e: Exception){
            publicacionNuevo.Precio         = etPrecio.text.toString().toInt().toDouble()
            e.printStackTrace()
        }
        Log.d("llego", "paso este punto15 ")
        publicacionNuevo.tieneCocina    = swCocina.isChecked
        Log.d("llego", "paso este punto16 ")
        publicacionNuevo.tieneComedor   = swComedor.isChecked
        Log.d("llego", "paso este punto17")
        ConfirmarPublicacion(publicacionNuevo)
    }
    fun ConfirmarPublicacion(publicacionConfirmada: PublicacionEntity){
        val DialogoConfirmacion = AlertDialog.Builder(this)
        DialogoConfirmacion.setTitle("¿Confirmar Venta?")
        DialogoConfirmacion.setMessage("Agregar esta publicacion a la venta")
        DialogoConfirmacion.setPositiveButton("OK"){dialog,id ->
            val Hilo2 = object:Thread(){
                override fun run() {
                    try {
                        DBAppTec.get(application).getPublicacionDAO().InsertPublicacion(publicacionConfirmada)
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            }
            Hilo2.start()
            Hilo2.join()
            Toast.makeText(this,"Publicacion Agregada", Toast.LENGTH_SHORT).show()
        }
        DialogoConfirmacion.setNegativeButton("Cancelar"){dialog, id ->
            Toast.makeText(this,"Publicacion Cancelada", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        DialogoConfirmacion.show()
    }
    override fun onResume() {
        super.onResume()
        ivCasaProfile.setImageURI(URI_PROFILE)
    }

}
