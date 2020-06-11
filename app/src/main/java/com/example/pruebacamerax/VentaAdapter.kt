package com.example.pruebacamerax

import android.content.Intent
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebacamerax.GlobalUser.Companion.direccionSelect

class VentaAdapter(val publicacionList: ArrayList<Publicacion>): RecyclerView.Adapter<VentaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_layout,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return publicacionList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val publi:Publicacion = publicacionList[position]
        holder.textViewDescripcion.text =   publi.descripcion
        holder.textViewPrecio.text =   "$ "+publi.precio.toString() + "MX"


        //holder.itemView.setOnClickListener(object: View.OnClickListener{
        //
        //})
        holder.itemView.setOnLongClickListener(object: View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                var menu = PopupMenu(v?.context, v)
                var menuInflater: MenuInflater = menu.menuInflater
                menuInflater.inflate(R.menu.popup_menu_lista, menu.menu)

                //accion a realizar al dar click al menu
                menu.setOnMenuItemClickListener(object: PopupMenu.OnMenuItemClickListener{
                    override fun onMenuItemClick(menuitem: MenuItem?): Boolean {
                        if (menuitem?.itemId == R.id.item_modificar){
                            direccionSelect = publi.descripcion.trim().toString()
                            val intent = Intent(v?.context, Compra_Activity::class.java)
                            v?.context?.startActivity(intent)
                        }
                        return false
                    }
                })
                //fin accion

                menu.show()

                return true
            }
        })
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textViewDescripcion = itemView.findViewById(R.id.descripcion) as TextView
        val textViewPrecio      = itemView.findViewById(R.id.precio) as TextView
    }


}