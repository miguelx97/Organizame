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
import com.miguelmartin.organizame.activities.DetalleActivity
import com.miguelmartin.organizame.activities.IMPORTANTE
import com.miguelmartin.organizame.bbdd.DB_TABLE_TAREAS
import com.miguelmartin.organizame.constantes.formatoFecha
import com.miguelmartin.organizame.constantes.formatoHora
import com.miguelmartin.organizame.model.Tarea


class AppViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item, parent, false)) {

    private var tvTitulo: TextView? = null
    private var tvDescripcion: TextView? = null
    private var tvHora: TextView? = null
    private var tvFecha: TextView? = null
    private var lyImportancia: LinearLayout? = null
    private var lyFechaHora: LinearLayout? = null
    private var lyBackground: LinearLayout? = null
    private var gradient:GradientDrawable? = null
//    lateinit var gradientDrawable: GradientDrawable
    lateinit var bgShape:GradientDrawable

    fun bind(tarea: Tarea) {

        tvTitulo = itemView.findViewById(R.id.tvTitulo)
        tvDescripcion = itemView.findViewById(R.id.tvDescripcion)
        tvHora = itemView.findViewById(R.id.tvHora)
        tvFecha = itemView.findViewById(R.id.tvFecha)
        lyImportancia = itemView.findViewById(R.id.lyImportancia)
        lyFechaHora = itemView.findViewById(R.id.lyFechaHora)
        lyBackground = itemView.findViewById(R.id.lyBackground)

        itemView.setOnClickListener {
            var intent = Intent(itemView.context, DetalleActivity::class.java)
            intent.putExtra(DB_TABLE_TAREAS, tarea)
            itemView.context.startActivity(intent)
        }
        tvTitulo?.text = tarea.titulo
        if(!tarea.descripcion.equals(""))tvDescripcion?.text = tarea.descripcion
        else tvDescripcion?.setVisibility(View.GONE)
        if(tarea.fecha != null) {
            tvHora?.text = formatoHora.format(tarea.fecha)
            tvFecha?.text = formatoFecha.format(tarea.fecha)
            if((tarea.fecha)!!.seconds == 0){
                tvHora?.setVisibility(View.GONE)
            }
        } else{
            lyFechaHora?.setVisibility(View.GONE)
        }

        if(tarea.prioridad == IMPORTANTE){
            lyImportancia?.setBackgroundResource(R.color.colorAccent)
        }
//tarea.categoria.color

        gradient = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0, 0, 0, tarea.categoria.color!!)
        )

        if(tarea.categoria.color == Color.TRANSPARENT){
            tarea.categoria.color = Color.WHITE
        }
        bgShape = lyBackground!!.getBackground() as GradientDrawable
        bgShape.setColor(tarea.categoria.color!!)

//        bgShape = GradientDrawable(
//            GradientDrawable.Orientation.LEFT_RIGHT,
//            intArrayOf(0, 0, 0, tarea.categoria.color!!)
//        )
//        lyBackground!!.setBackground(bgShape)


    }

}