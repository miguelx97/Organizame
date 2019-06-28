package com.miguelmartin.organizame.data

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.miguelmartin.organizame.R
import com.miguelmartin.organizame.activities.AddTareaActivity
import com.miguelmartin.organizame.activities.IMPORTANTE
import com.miguelmartin.organizame.bbdd.DB_TABLE_TAREAS
import com.miguelmartin.organizame.Util.formatoFecha
import com.miguelmartin.organizame.Util.formatoHora
import com.miguelmartin.organizame.Util.formatoSegundos
import com.miguelmartin.organizame.model.Tarea
import java.text.SimpleDateFormat


class AppViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item, parent, false)) {

    private lateinit var tvTitulo: TextView
    private lateinit var tvDescripcion: TextView
    private lateinit var tvHora: TextView
    private lateinit var tvFecha: TextView
    private lateinit var lyImportancia: LinearLayout
    private lateinit var lyFechaHora: LinearLayout
    private lateinit var lyBackground: LinearLayout
    private lateinit var tvCategoria:TextView
    private lateinit var lyCategoria: LinearLayout
    private lateinit var lyInferior: LinearLayout
    private lateinit var tvCategoria2:TextView

    private lateinit var descripcion:String
    private var maxLengh = 30


    fun bind(tarea: Tarea) {

        tvTitulo = itemView.findViewById(R.id.tvTitulo)
        tvDescripcion = itemView.findViewById(R.id.tvDescripcion)
        tvHora = itemView.findViewById(R.id.tvHora)
        tvFecha = itemView.findViewById(R.id.tvFecha)
        lyImportancia = itemView.findViewById(R.id.lyImportancia)
        lyFechaHora = itemView.findViewById(R.id.lyFechaHora)
        lyBackground = itemView.findViewById(R.id.lyBackground)
        tvCategoria = itemView.findViewById(R.id.tvCategoria)
        lyCategoria = itemView.findViewById(R.id.lyCategoria)
        lyInferior = itemView.findViewById(R.id.lyInferior)
        tvCategoria2 = itemView.findViewById(R.id.tvCategoria2)


        itemView.setOnClickListener {
            var intent = Intent(itemView.context, AddTareaActivity::class.java)
            intent.putExtra(DB_TABLE_TAREAS, tarea)
            itemView.context.startActivity(intent)
        }
        tvTitulo.text = tarea.titulo

        descripcion = tarea.descripcion!!
        if(descripcion.length > maxLengh){
            descripcion = descripcion.substring(0,maxLengh-3)+"..."
        } else{

        }
//        descripcion = tarea.estado.toString()   //TODO: QUITAR
        if(!descripcion.equals("")){
            tvDescripcion.text = descripcion
        } else{
            tvDescripcion.setVisibility(View.GONE)
        }



        if(tarea.fecha != null) {
            tvHora.text = formatoHora.format(tarea.fecha)
            tvFecha.text = formatoFecha.format(tarea.fecha)
            if(formatoSegundos.format((tarea.fecha)!!).toInt() == 0){
                tvHora.setVisibility(View.GONE)
            }
        } else{
            tvHora.setVisibility(View.GONE)
            tvFecha.setVisibility(View.GONE)
        }

        if(tarea.prioridad == IMPORTANTE){
            lyImportancia.setBackgroundResource(R.color.colorAccent)
        }



        if(tarea.fecha == null){
            lyCategoria.visibility = View.VISIBLE
            tvCategoria.visibility = View.GONE
            tvCategoria2.text = tarea.categoria.titulo
            tvCategoria2.setBackgroundColor(tarea.categoria.color!!)
            if(descripcion.isEmpty()){
                lyInferior.visibility = View.GONE
            }
        } else{
            lyCategoria.visibility = View.GONE
            lyInferior.visibility = View.VISIBLE
            tvCategoria.text = tarea.categoria.titulo
            tvCategoria.setBackgroundColor(tarea.categoria.color!!)
            if(descripcion.isEmpty() && tarea.categoria.id == 0){
                lyInferior.visibility = View.GONE
            }
        }

    }

}