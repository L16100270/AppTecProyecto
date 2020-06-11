package com.example.pruebacamerax

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.pruebacamerax.entidades.PublicacionEntity
import com.example.pruebacamerax.roomdb.DBAppTec
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_ventas.*

class ventas : AppCompatActivity() {


    var recycleVentas : RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventas)
         recycleVentas               = findViewById(R.id.recyclerView) as RecyclerView

        recycleVentas?.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        //traer los datos de Room:
        var publicacionesRoom : List<PublicacionEntity>? = null
        val HiloPublicaciones = object:Thread(){
            override fun run() {
                try{
                    publicacionesRoom = DBAppTec.get(application).getPublicacionDAO().getAllPublicacion()
                }catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
        }
        HiloPublicaciones.start()
        HiloPublicaciones.join()
        var publicaciones = ArrayList<Publicacion>()
        //publicaciones.add(Publicacion("Casa Bonita",1500.0))
        if (publicacionesRoom?.size!! > 0) {
            publicacionesRoom!!.forEach {
                if(it.Vendida == false) {
                    publicaciones.add(Publicacion(it.Direccion, it.Precio))
                }
            }
        }


        var adapterRecycler = VentaAdapter(publicaciones)
        recycleVentas?.adapter =  adapterRecycler

        //SnackBar
        fabAction.setOnClickListener {
            //snackbar
        val sb = Snackbar.make(findViewById(R.id.layout_general), "Agregar Publicacion",Snackbar.LENGTH_LONG)
            .setAction("Agregar"){
                val intent = Intent(this, Registro_publicacion::class.java)
                startActivity(intent)
            }
            sb.show()


        }
        //fin snack bar

        //Refrescar Recycler View
        swipeRefresh.setOnRefreshListener ( object: SwipeRefreshLayout.OnRefreshListener{
                override fun onRefresh() {

                    val HiloPublicaciones = object:Thread(){
                        override fun run() {
                            try{
                                publicacionesRoom = DBAppTec.get(application).getPublicacionDAO().getAllPublicacion()
                            }catch (e: Exception)
                            {
                                e.printStackTrace()
                            }
                        }
                    }
                    HiloPublicaciones.start()
                    HiloPublicaciones.join()
                    publicaciones = ArrayList<Publicacion>()
                    //publicaciones.add(Publicacion("Casa Bonita",1500.0))
                    if (publicacionesRoom?.size!! > 0) {
                        publicacionesRoom!!.forEach {
                            if(it.Vendida == false) {
                                publicaciones.add(Publicacion(it.Direccion, it.Precio))
                            }
                        }
                    }

                    swipeRefresh.isRefreshing = false
                    adapterRecycler = VentaAdapter(publicaciones)
                    recycleVentas?.adapter =  adapterRecycler
                } //end onRefresh
            }
        )

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
        if(id == R.id.item_cuenta){
            goToAccountActivity()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun goToPreferencesActivity(){
        startActivity(Intent(this, PreferencesActivity::class.java))
    }
    private fun goToAccountActivity(){
        startActivity(Intent(this, CuentaActivity::class.java))
    }
}

