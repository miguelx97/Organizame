package com.miguelmartin.organizame.data

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.miguelmartin.organizame.data.AppViewHolder
import com.miguelmartin.organizame.model.Categoria

class AppAdapterCategorias(private val list: List<Categoria>)
    : RecyclerView.Adapter<AppViewHolderCategorias>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolderCategorias {
        val inflater = LayoutInflater.from(parent.context)
        return AppViewHolderCategorias(inflater, parent)
    }

    override fun onBindViewHolder(holder: AppViewHolderCategorias, position: Int) {
        val categoria: Categoria = list[position]
        holder.bind(categoria)
    }

    override fun getItemCount(): Int = list.size

}