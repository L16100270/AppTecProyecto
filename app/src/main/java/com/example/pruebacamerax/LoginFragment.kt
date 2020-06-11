package com.example.pruebacamerax

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.pruebacamerax.GlobalUser.Companion.usuarioLogueado
import com.example.pruebacamerax.entidades.UsuarioEntity
import com.example.pruebacamerax.roomdb.DBAppTec
import kotlinx.android.synthetic.main.fragment_login.*
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

     var btnRegistrarmeFragment: Button?    = null
     var btnIngresoFragment: Button?        = null
     var etCorreoFragment: EditText?        = null
     var etPasswordFragment: EditText?      = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        // btnIngresar.setOnClickListener { Toast.makeText(activity,"Ingresar Login", Toast.LENGTH_SHORT).show() }
        var viewLogin:View = inflater.inflate(R.layout.fragment_login, container, false)
        btnRegistrarmeFragment =  viewLogin.findViewById(R.id.btnRegistrarme) as Button
        btnIngresoFragment     =  viewLogin.findViewById(R.id.btnIngresar) as Button
        etCorreoFragment       =  viewLogin.findViewById(R.id.etCorreoIngreso) as EditText
        etPasswordFragment     =  viewLogin.findViewById(R.id.etPassword) as EditText
        btnRegistrarmeFragment!!.setOnClickListener { AbrirRegistro() }
        btnIngresoFragment!!.setOnClickListener { ComprobarLogIn() }
        return viewLogin
    }
    fun ComprobarLogIn() {
        var correcto:Boolean = false
        var existe:Boolean = false
        var usuarioLogin:UsuarioEntity? = UsuarioEntity()
        if (etCorreoFragment!!.text.trim().toString().isNullOrEmpty()){
            Toast.makeText(activity,"Ingrese Correo", Toast.LENGTH_SHORT).show()
            return
        }
        if(etPasswordFragment!!.text.trim().toString().isNullOrEmpty()){
            Toast.makeText(activity,"Ingresar Contraseña", Toast.LENGTH_SHORT).show()
            return
        }
        var Correo:String   = etCorreoFragment!!.text.trim().toString()
        var Password:String = etPasswordFragment!!.text.trim().toString()
        val HiloLogin = object: Thread(){
            override fun run(){
                try {
                    usuarioLogin = DBAppTec.get(activity!!.application).getUsuarioDAO().getUsuario(Correo).firstOrNull()
                }catch(e: Exception){
                    e.printStackTrace()
                }
            }
        }
        HiloLogin.start()
        HiloLogin.join()

        if( usuarioLogin == null){
            Toast.makeText(activity,"Este correo no se ha registrado", Toast.LENGTH_SHORT).show()
            return
        }
        if(usuarioLogin!!.Password != Password)
        {
            Toast.makeText(activity,"Contraseña Incorrecta!", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(activity,"Login Correcto!", Toast.LENGTH_SHORT).show()
        usuarioLogueado = usuarioLogin!!
        //if(usuarioLogin!!.Vendedor){
        //    AbrirScrollVendedor()
        //}else{
        //   AbrirScrollUsuario()
       //}
        AbrirCuenta()
    }
     fun AbrirRegistro() {
    val intent = Intent(activity, Registro_Activity::class.java)
        startActivity(intent)
    }
    fun AbrirCuenta(){
        val intent = Intent(activity, ventas::class.java)
       // val intent = Intent(activity, CuentaActivity::class.java)
        startActivity(intent)
    }
    fun AbrirScrollVendedor(){
        val intent = Intent(activity, ScrollSeller_Activity::class.java)
        startActivity(intent)
    }
    fun AbrirScrollUsuario(){
        val intent = Intent(activity, ScrollUser_Activity::class.java)
        startActivity(intent)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
