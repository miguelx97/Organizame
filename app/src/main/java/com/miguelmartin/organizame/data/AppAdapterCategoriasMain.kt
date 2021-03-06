package com.miguelmartin.organizame.data

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.miguelmartin.organizame.R
import com.miguelmartin.organizame.activities.MainActivity
import com.miguelmartin.organizame.model.Categoria


class AppAdapterCategoriasMain(private val list: List<Categoria>)
    : RecyclerView.Adapter<AppAdapterCategoriasMain.AppAdapterCategoriasMain>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppAdapterCategoriasMain {
        val inflater = LayoutInflater.from(parent.context)
        return AppAdapterCategoriasMain(inflater, parent)
    }

    override fun onBindViewHolder(holder: AppAdapterCategoriasMain, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
    inner class AppAdapterCategoriasMain(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_categoria_main, parent, false)) {

        private var tvCategoria: TextView? = null

        val mainActivity = MainActivity()

        fun bind(categoria: Categoria) {

            tvCategoria = itemView.findViewById(R.id.tvCategoria)
            tvCategoria?.text = categoria.titulo
            tvCategoria?.setBackgroundColor(categoria.color!!)

            itemView.setOnClickListener {
                mainActivity.setIdCategoria(categoria.id)
                mainActivity.cargarItems()
            }

        }

    }



}