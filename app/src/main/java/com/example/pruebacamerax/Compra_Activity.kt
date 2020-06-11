package com.example.pruebacamerax

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.pruebacamerax.GlobalUser.Companion.direccionSelect
import com.example.pruebacamerax.entidades.PublicacionEntity
import com.example.pruebacamerax.roomdb.DBAppTec
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_compra_.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.round

class Compra_Activity : AppCompatActivity() {
    var publicacionActual: PublicacionEntity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compra_)
        //rellenar campos
        var direccionActual:String = direccionSelect
        //Busca Publicacion
        val Hilo = object:Thread(){
            override fun run() {
                try {
                    publicacionActual = DBAppTec.get(application).getPublicacionDAO().
                    getPublicacion(direccionActual.trim().toString())[0]
                }catch(e: Exception)
                {
                    e.printStackTrace()
                }
            }
        }
        Hilo.start()
        Hilo.join()
        //cargar los datos de la casa
        CargaCasa()
        btnComprarC.setOnClickListener {
            ConfirmarCompra()
        }
        btnContactarC.setOnClickListener {
            Contactar()
        }

        swDolares.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                PeticionWS()
            }else{
                txtPrecioC.text = "$ "+publicacionActual?.Precio.toString() + " MX "
            }
        }
    }

    fun PeticionWS(){
        var tipoCambioR = 0.0
        val retrofit =  Retrofit.Builder()
            .baseUrl("https://tareatec.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        val TCWebService = retrofit.create(TCRegreso::class.java)
        val TipoCambioReal = TCWebService.getTCReal()

        var HiloTC = object:Thread() {
            override fun run() {
                try {

                    TipoCambioReal.enqueue(
                        object : Callback<String> {
                            override fun onFailure(call: Call<String>, t: Throwable) {
                                Snackbar.make(
                                    window.decorView.rootView,
                                    "Fallo la petición tipo cambio",
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction("Action", null).show()
                                var HiloSwitch = object:Thread(){
                                    override fun run() {
                                        try{
                                            //Thread.sleep(2000)
                                           // swDolares.isChecked = false
                                        }
                                        catch (e: Exception){
                                            e.printStackTrace()
                                        }
                                    }
                                }
                                HiloSwitch.start()
                                Log.e("tcreal error", t.message)
                            }

                            override fun onResponse(
                                call: Call<String>,
                                response: Response<String>
                            ) {
                                var Valor:String = "${response.body()}"
                                tipoCambioR = Valor.toDouble()
                                var valorEnDolares = "%.2f".format(publicacionActual?.Precio!! / tipoCambioR)
                                txtPrecioC.text = "$ "+valorEnDolares.toString() + " USD "
                                // for  (BMX in response.body()!!) falla tipo cambio Banxico
                                //obtiene tc Banxico
                                //BMX.series[0].datos[0].tipocambio
                                // }
                            }
                        })

                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        HiloTC.start()
        HiloTC.join()
    }
    fun Contactar(){
        val intent = Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+publicacionActual?.Contacto))
        startActivity(intent)
    }

    fun ConfirmarCompra(){
        val DialogoConfirmacion = AlertDialog.Builder(this)
        DialogoConfirmacion.setTitle("¿Confirmar Compra?")
        DialogoConfirmacion.setMessage("Confirmar la compra de esta Vivienda")
        DialogoConfirmacion.setPositiveButton("OK"){dialog,id ->
            //actualizar casa a  vendida
            publicacionActual?.Vendida = true
            var HiloCompra = object:Thread(){
                override fun run() {
                  try {
                      DBAppTec.get(application).getPublicacionDAO().updatePublicacion(publicacionActual!!)
                  }catch (e: Exception){
                      e.printStackTrace()
                  }
                }
            }
            HiloCompra.start()
            HiloCompra.join()
            Toast.makeText(this,"Compra Confirmada", Toast.LENGTH_SHORT).show()
        }
        DialogoConfirmacion.setNegativeButton("Cancelar"){dialog, id ->
            //no hace nada
            Toast.makeText(this,"Compra Cancelada", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        DialogoConfirmacion.show()
    }

    fun CargaCasa(){
        ivImagenCasaC.setImageURI(Uri.parse(publicacionActual?.ImagenCasa))
        txtDireccionC.text = publicacionActual?.Direccion
        txtHabitacionesC.text = publicacionActual?.Habitaciones.toString()
        txtBanosC.text = publicacionActual?.Banos.toString()
        if(publicacionActual?.tieneCocina!!){
            txtCocinaC.text = "Incluye Cocina"
        }
        else{
            txtCocinaC.text = "No Incluye Cocina"
        }
        if(publicacionActual?.tieneComedor!!){
            txtComedorC.text = "Incluye Comedor"
        }else{
            txtComedorC.text = "No Incluye Comedor"
        }

        txtPrecioC.text = "$ "+publicacionActual?.Precio.toString() + " MX "
    }

}
