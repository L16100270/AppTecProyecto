package com.example.pruebacamerax

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.pruebacamerax.GlobalUser.Companion.datosReg
import com.example.pruebacamerax.GlobalUser.Companion.fileNameReg
import java.io.IOException
import java.io.OutputStreamWriter

class MyService : Service() {
    val TAG = "MyService"
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        ShowLog("onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        ShowLog("onStartCommand")
        val proceso = Runnable {
            //var informacion:String?   = intent?.getStringExtra("datos")
           // var nombreArchivo:String = intent?.getStringExtra("fileName")!!
            GuardarRespaldoUsuario(datosReg,fileNameReg)
            stopSelf()
        }
         val thread = Thread(proceso)
        thread.start()


        return super.onStartCommand(intent, flags, startId)
    }
    fun GuardarRespaldoUsuario(datos: String, fileName: String){
        try {
            val archivo = OutputStreamWriter(openFileOutput("${fileName}.txt", Activity.MODE_PRIVATE))
            archivo.write(datos)
            archivo.flush()
            archivo.close()
        } catch (e: IOException) {
            //no se realizo cpia de datos en memoria Interna
        }
    }

    override fun onDestroy() {
        ShowLog("onDestroy")
        super.onDestroy()
    }

    fun ShowLog(message: String){
        Log.d(TAG,message)
    }
}
