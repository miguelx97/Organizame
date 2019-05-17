package com.miguelmartin.organizame

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.miguelmartin.organizame.entity.Tarea

class AppAdapter(private val list: List<Tarea>)
    : RecyclerView.Adapter<TareaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {    //https://medium.com/@hinchman_amanda/working-with-recyclerview-in-android-kotlin-84a62aef94ec
        val inflater = LayoutInflater.from(parent.context)
        return TareaViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea: Tarea = list[position]
        holder.bind(tarea)
    }

    override fun getItemCount(): Int = list.size

}