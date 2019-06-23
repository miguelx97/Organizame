package com.miguelmartin.organizame.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.miguelmartin.organizame.R
import com.miguelmartin.organizame.activities.AddTareaActivity
import com.miguelmartin.organizame.activities.MainActivity
import com.miguelmartin.organizame.bbdd.DB_TABLE_TAREAS
import com.miguelmartin.organizame.bbdd.DbPersistenciaTareas
import com.miguelmartin.organizame.model.Categoria
import com.miguelmartin.organizame.model.Tarea


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
        private val dbPersistencia:DbPersistenciaTareas = DbPersistenciaTareas(itemView.context)
        lateinit var tareas:List<Tarea>
        val rvTareas: RecyclerView = (itemView.context as Activity).findViewById(R.id.rvTareas) as RecyclerView

        fun bind(categoria: Categoria) {

            tvCategoria = itemView.findViewById(R.id.tvCategoria)


            tvCategoria?.text = categoria.titulo
            tvCategoria?.setBackgroundColor(categoria.color!!)

            itemView.setOnClickListener {
                rellenarTareasByCategorias(arrayOf(categoria.id.toString()), itemView.context)
            }

        }

        fun rellenarTareasByCategorias(arrCategorias: Array<String>, context: Context){
            tareas = dbPersistencia.getTaresByCategoria(arrCategorias)
            rvTareas.layoutManager = LinearLayoutManager(context)
            val adapter = AppAdapter(tareas)
            rvTareas.adapter = adapter
        }

    }



}