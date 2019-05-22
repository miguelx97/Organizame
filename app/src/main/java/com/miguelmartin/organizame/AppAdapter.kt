package com.miguelmartin.organizame

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.miguelmartin.organizame.entity.Tarea

class AppAdapter(private val list: List<Tarea>)
    : RecyclerView.Adapter<AppViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AppViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val tarea: Tarea = list[position]
        holder.bind(tarea)
    }

    override fun getItemCount(): Int = list.size

}