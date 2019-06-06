package com.miguelmartin.organizame.data

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.miguelmartin.organizame.R
import com.miguelmartin.organizame.model.Categoria

class AppViewHolderCategorias(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_categoria, parent, false)) {

    private var tvTitulo: TextView? = null

    fun bind(categoria: Categoria) {

        tvTitulo = itemView.findViewById(R.id.tvTitulo)

        tvTitulo?.text = categoria.titulo

    }

}