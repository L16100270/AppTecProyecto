package com.example.pruebacamerax

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private var mNotificationManager: NotificationManager? = null
    private val NOTIFICATION_ID = 0
    private val PRIMARY_CHANNEL_ID = "primary_notification_channel2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Inicializa Notificacion
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
        //PeticionWS()
        //comenzar servicio
    }
    fun RevisarAlarma(Encender: Boolean){
        //comienza alarma
        val notifyIntent = Intent(this, AlarmReceiver::class.java)
        val alarmUp = PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_NO_CREATE) != null
        val notifyPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val repeatInterval:Long = 1000 * 30   //30 segundos
        val triggerTime = SystemClock.elapsedRealtime() + repeatInterval
        if(Encender) {
            //activat la alarma
            alarmManager?.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, repeatInterval, notifyPendingIntent
            )
        }else{
            //desactivar la alarma
            mNotificationManager!!.cancelAll()
            alarmManager?.cancel(notifyPendingIntent)
        }

    }

    fun createNotificationChannel() {
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        //los canales estan disponibles solo desde la version Oreo en adelante, asi que validamos la version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(PRIMARY_CHANNEL_ID, "Notificacion AppTec", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Sigue usando nuestra APP!"
            mNotificationManager!!.createNotificationChannel(notificationChannel)
        }
    }

    fun PeticionWS(){
        val retrofit =  Retrofit.Builder()
            .baseUrl("https://tareatec.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        val TCWebService = retrofit.create(TCRegreso::class.java)
        val TipoCambioReal = TCWebService.getTCReal()

        TipoCambioReal.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Snackbar.make(window.decorView.rootView, "Fallo la petición", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                Log.e("tcreal error", t.message)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
               var Valor = response.body()
                Log.e("tcreal valor", "${Valor}")
                // for  (usuario in response.body()!!)
               // }
            }
        })

    }

    fun ConfirmarSalida(){
        val DialogoConfirmacion = AlertDialog.Builder(this)
        DialogoConfirmacion.setTitle("¿Confirmar Salir?")
        DialogoConfirmacion.setMessage("Desea Salir de la Aplicacion")
        DialogoConfirmacion.setPositiveButton("OK"){dialog,id ->
            //traer la preferencia de Notificacion
            var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            var encenderAlarma = sharedPreferences.getBoolean("switch_preference",false)
            Log.e("sharedP","valor: ${encenderAlarma.toString()}")
            RevisarAlarma(encenderAlarma)
                finish()
        }
        DialogoConfirmacion.setNegativeButton("Cancelar"){dialog, id ->
            dialog.cancel()
        }
        DialogoConfirmacion.show()
    }
    override fun onBackPressed() {
        ConfirmarSalida()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_preferencias,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.item_preference){
            goToPreferencesActivity()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun goToPreferencesActivity(){
        startActivity(Intent(this, PreferencesActivity::class.java))
    }
}