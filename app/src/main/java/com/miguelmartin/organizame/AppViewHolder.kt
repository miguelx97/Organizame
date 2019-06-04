package com.miguelmartin.organizame

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.miguelmartin.organizame.bbdd.DB_TABLE_TAREAS
import com.miguelmartin.organizame.constantes.formatoFecha
import com.miguelmartin.organizame.constantes.formatoHora
import com.miguelmartin.organizame.entity.Tarea
import java.text.SimpleDateFormat

class AppViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item, parent, false)) {

    private var tvTitulo: TextView? = null
    private var tvDescripcion: TextView? = null
    private var tvHora: TextView? = null
    private var tvFecha: TextView? = null
    private var lyImportancia: LinearLayout? = null
    private var lyFechaHora: LinearLayout? = null

    fun bind(tarea: Tarea) {

        tvTitulo = itemView.findViewById(R.id.tvTitulo)
        tvDescripcion = itemView.findViewById(R.id.tvDescripcion)
        tvHora = itemView.findViewById(R.id.tvHora)
        tvFecha = itemView.findViewById(R.id.tvFecha)
        lyImportancia = itemView.findViewById(R.id.lyImportancia)
        lyFechaHora = itemView.findViewById(R.id.lyFechaHora)

        itemView.setOnClickListener {
            var intent = Intent(itemView.context, DetalleActivity::class.java)
            intent.putExtra(DB_TABLE_TAREAS, tarea)
            itemView.context.startActivity(intent)
        }
        tvTitulo?.text = tarea.titulo
        if(!tarea.descripcion.equals(""))tvDescripcion?.text = tarea.descripcion
        else tvDescripcion?.setVisibility(View.GONE)
        if(tarea.fecha != null) {
            tvHora?.text = formatoHora.format(tarea.fecha)
            tvFecha?.text = formatoFecha.format(tarea.fecha)
            if((tarea.fecha)!!.seconds == 0){
                tvHora?.setVisibility(View.GONE)
            }
        } else{
            lyFechaHora?.setVisibility(View.GONE)
        }

        if(tarea.prioridad == 2){
            lyImportancia?.setBackgroundResource(R.color.colorAccent)
        }
    }

}