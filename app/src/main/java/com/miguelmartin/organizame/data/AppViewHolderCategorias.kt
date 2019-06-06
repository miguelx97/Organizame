package com.miguelmartin.organizame.data

import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.miguelmartin.organizame.R
import com.miguelmartin.organizame.model.Categoria
import kotlinx.android.synthetic.main.activity_gestion_categorias.*


class AppViewHolderCategorias(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_categoria, parent, false)) {

    private var tvTitulo: TextView? = null
    private var lyCategoria: LinearLayout? = null

//    var gradientDrawable: GradientDrawable = itemView.getBackground().mutate() as GradientDrawable

    fun bind(categoria: Categoria) {

        tvTitulo = itemView.findViewById(R.id.tvTitulo)
        lyCategoria = itemView.findViewById(R.id.lyCategoria)
//        gradientDrawable = lyCategoria!!.getBackground().mutate() as GradientDrawable
//        gradientDrawable.setColor(categoria.color!!)
//        lyCategoria.setBackgroundResource(R.drawable.papelera)

        Log.w("ID: $categoria.id", "color: " +categoria.color)
        //gradientDrawable.setColor(categoria.color!!)
        itemView.setBackgroundColor(categoria.color!!)

        tvTitulo?.text = categoria.titulo



    }


}