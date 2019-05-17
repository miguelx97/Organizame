package com.miguelmartin.organizame

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.miguelmartin.organizame.entity.Tarea

class TareaViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item, parent, false)) {
    private var tvTitulo: TextView? = null
    private var tvDescripcion: TextView? = null


    init {
        tvTitulo = itemView.findViewById(R.id.tvTitulo)
        tvDescripcion = itemView.findViewById(R.id.tvDescripcion)
    }

    fun bind(tarea: Tarea) {
        tvTitulo?.text = tarea.titulo
        tvDescripcion?.text = tarea.descripcion
    }

}