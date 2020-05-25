package com.example.pruebacamerax

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pruebacamerax.roomdb.DBAppTec
import java.lang.Exception

class Splash_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_)

        //Definir hilo para dormir splash
        val ControlSplash  = object :Thread() {
            override fun run(){
                try {
                    Thread.sleep(3000)
                    val intent = Intent(baseContext, MainActivity::class.java)
                    startActivity(intent)
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        //dormir splash
        DBAppTec.get(application)
        ControlSplash.start()
    }
}
