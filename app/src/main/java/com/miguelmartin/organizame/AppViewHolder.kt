package com.miguelmartin.organizame

import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.miguelmartin.organizame.bbdd.DB_TABLE
import com.miguelmartin.organizame.entity.Tarea
import java.text.SimpleDateFormat

class AppViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item, parent, false)) {

    private var tvTitulo: TextView? = null
    private var tvDescripcion: TextView? = null
    private var tvHora: TextView? = null
    private var tvFecha: TextView? = null
    private var lyImportancia: LinearLayout? = null

    fun bind(tarea: Tarea) {

        tvTitulo = itemView.findViewById(R.id.tvTitulo)
        tvDescripcion = itemView.findViewById(R.id.tvDescripcion)
        tvHora = itemView.findViewById(R.id.tvHora)
        tvFecha = itemView.findViewById(R.id.tvFecha)
        lyImportancia = itemView.findViewById(R.id.lyImportancia)

        itemView.setOnClickListener {
            var intent = Intent(itemView.context, AddTareaActivity::class.java)
            intent.putExtra(DB_TABLE, tarea)
            itemView.context.startActivity(intent)
        }
        tvTitulo?.text = tarea.titulo
        tvDescripcion?.text = tarea.descripcion
        tvDescripcion?.text = tarea.descripcion
        tvHora?.text = SimpleDateFormat("hh:mm").format(tarea.fecha)
        tvFecha?.text = SimpleDateFormat("dd MMM").format(tarea.fecha)

        if(tarea.prioridad == 2){
            lyImportancia?.setBackgroundResource(R.color.colorAccent)
        }
    }

}