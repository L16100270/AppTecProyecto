package com.example.pruebacamerax

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.AppLaunchChecker
import androidx.core.content.FileProvider
import com.example.pruebacamerax.entidades.UsuarioEntity
import com.example.pruebacamerax.fragments.CameraFragment.Companion.URI_PROFILE
import com.example.pruebacamerax.roomdb.DBAppTec
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registro_.*
import java.io.*
import java.security.AccessController.getContext
import java.util.*

class Registro_Activity : AppCompatActivity() {
    var Correo      :String = ""
    var Password    :String = ""
    var Nombre      :String = ""
    var Apellido    :String = ""
    var Telefono    :String = ""
    var Vendedor    :Boolean = false
    var rutaImagen  :String = ""
    val PHOTO_CONSTANT:Int = 1

    var CARPETA_PRINCIPAL:String = "misImagenesApp/"
    var CARPETA_IMAGEN:String = "imagenes"
    var DIRECTORIO_IMAGEN:String = CARPETA_PRINCIPAL + CARPETA_IMAGEN
    var path:String = ""
    var fileImagen:File?= null
    var bitmapImagen:Bitmap? = null
    var COD_SELECCIONA:Int = 10
    var COD_FOTO:Int = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_)
        ivProfile.setOnClickListener{
            AbrirCamara()
            //dispatchTakePictureIntent()
        }
        btnRegistrar.setOnClickListener {
            Registrar()
        }
        var options = getContacts()
        spTelefono.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,options)
        spTelefono.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                etNumTelefono.setText("")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                etNumTelefono.setText(options.get(position))
            }

        }
        //abrir

        //abrir
    }
    //pasar nombre del archivo con la extencion, ejemplo: "nombre.txt"
    fun LeerInformacion(nombreArchivo: String): String{
        var Data:String = ""
        if (fileList().contains(nombreArchivo)) {
            try {
                val archivo = InputStreamReader(openFileInput(nombreArchivo))
                val br = BufferedReader(archivo)
                var linea = br.readLine()
                val todo = StringBuilder()
                while (linea != null) {
                    todo.append(linea + "\n")
                    linea = br.readLine()
                }
                br.close()
                archivo.close()
                Data = todo.toString()
            } catch (e: IOException) {
            }
        }
        return Data
    }

    fun GuardarInformacion(datos: String, fileName: String){
        try {
            val archivo = OutputStreamWriter(openFileOutput("${fileName}.txt", Activity.MODE_PRIVATE))
            archivo.write(datos)
            archivo.flush()
            archivo.close()
        } catch (e: IOException) {
        }
    }
    fun Registrar()
    {
        var existe: Boolean = false
        var cant: Int = 0
        //Valida
        if (etCorreo.text.trim().isNullOrEmpty()) {
            Toast.makeText(this,"Ingrese Correo", Toast.LENGTH_SHORT).show()
            return
        }
        val Hilo = object:Thread(){
            override fun run() {
                try {
                    existe = DBAppTec.get(application).getUsuarioDAO()
                        .getUsuario(etCorreo.text.trim().toString()).size > 0
                }catch(e: Exception)
                {
                    e.printStackTrace()
                }
            }
        }
        Hilo.start()
        Hilo.join()

        if (existe){
            Toast.makeText(this,"Ya existe usuario llamado: ${etCorreo.text.trim().toString()}", Toast.LENGTH_SHORT).show()
            return
        }
        if (etContra.text.trim().isNullOrEmpty()) {
            Toast.makeText(this,"Ingrese Contraseña", Toast.LENGTH_SHORT).show()
            return
        }
        if(etContra2.text.trim().isNullOrEmpty()){
            Toast.makeText(this,"Confirme Contraseña", Toast.LENGTH_SHORT).show()
            return
        }
        if(etContra.text.toString() != etContra2.text.toString()){
            Toast.makeText(this,"Contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }
        if(etName.text.trim().isNullOrEmpty()){
            Toast.makeText(this,"Ingrese Nombre", Toast.LENGTH_SHORT).show()
            return
        }
        if(etApellido.text.trim().isNullOrEmpty()){
            Toast.makeText(this,"Ingrese Apellido", Toast.LENGTH_SHORT).show()
            return
        }
        if(etNumTelefono.text.trim().isNullOrEmpty()){
            Toast.makeText(this,"Ingrese Numero Telefonico", Toast.LENGTH_SHORT).show()
            return
        }
        if(URI_PROFILE == null)
        {
            Toast.makeText(this,"Capture una Foto de Perfil", Toast.LENGTH_SHORT).show()
            return
        }

        var usuarioNuevo = UsuarioEntity()
        usuarioNuevo.Correo         = etCorreo.text.trim().toString()
        usuarioNuevo.Password       = etContra.text.toString()
        usuarioNuevo.Nombre         = etName.text.trim().toString()
        usuarioNuevo.Apellido       = etApellido.text.trim().toString()
        usuarioNuevo.Telefono       = etNumTelefono.text.trim().toString()
        usuarioNuevo.Vendedor       = ckOfertar.isChecked
        usuarioNuevo.ImagenPerfil   =  URI_PROFILE.toString() //variable global
        Thread {
            DBAppTec.get(application).getUsuarioDAO().InsertUsuario(usuarioNuevo)
            DBAppTec.get(application).getUsuarioDAO().getAllUsuario().forEach {
                Log.d("agregado", "Correo : ${it.Correo}")
                Log.d("agregado", "Nombre : ${it.Nombre}")
                Log.d("agregado", "Password : ${it.Password}")
                Log.d("agregado", "Apellido : ${it.Apellido}")
                Log.d("agregado", "Vendedor : ${it.Vendedor}")
                Log.d("agregado", "Telefono : ${it.Telefono}")
            }
        }.start()
        //Guardar informacion en memoria interna
        var info = usuarioNuevo.Correo +"\n"+ usuarioNuevo.Nombre + "\n" + usuarioNuevo.Password +
                "\n" + usuarioNuevo.Apellido + "\n" + usuarioNuevo.Vendedor.toString() +"\n"+ usuarioNuevo.Telefono
        GuardarInformacion(info,usuarioNuevo.Correo)
        //notifica usuario registrado
        Toast.makeText(this,"Usuario Registrado", Toast.LENGTH_SHORT).show()
       AbrirLogin()
    }

    //traer contactos de content provider
    fun append(arr: Array<String>, element: String): Array<String> {
        val list: MutableList<String> = arr.toMutableList()
        list.add(element)
        return list.toTypedArray()
    }
    fun AbrirLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    fun getContacts(): Array<String> {
        var numeros: Array<String> = arrayOf("")
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null)
        if(cursor != null && cursor.moveToFirst()){
            do {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val phoneNumber = (cursor.getString( cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()
                //
                if (phoneNumber > 0) {
                    val cursorPhone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)
                    if(cursorPhone != null && cursorPhone.moveToFirst())
                    {
                        do{
                            val phoneNumValue = cursorPhone.getString( cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            numeros = append(numeros,phoneNumValue)
                            Log.d("celular", "telefono es : ${phoneNumValue}")
                        }while (cursorPhone.moveToNext())
                        cursorPhone.close()
                    }
                }
                //
            }while (cursor.moveToNext())
            cursor.close()
        }
        return numeros
    }

    override fun onResume() {
        super.onResume()
        ivProfile.setImageURI(URI_PROFILE)
    }
    fun AbrirCamara()
    {
        val intentAct = Intent(this,FotoPerfilActivity::class.java)
        startActivity(intentAct)

    }
}
