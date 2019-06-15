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

    private lateinit var tvTitulo: TextView
    private lateinit var tvDescripcion: TextView
    private lateinit var tvHora: TextView
    private lateinit var tvFecha: TextView
    private lateinit var lyImportancia: LinearLayout
    private lateinit var lyFechaHora: LinearLayout
    private lateinit var lyBackground: LinearLayout
    private lateinit var gradient:GradientDrawable
    private lateinit var bgShape:GradientDrawable

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

        itemView.setOnClickListener {
            var intent = Intent(itemView.context, DetalleActivity::class.java)
            intent.putExtra(DB_TABLE_TAREAS, tarea)
            itemView.context.startActivity(intent)
        }
        tvTitulo?.text = tarea.titulo

        descripcion = tarea.descripcion!!
        if(descripcion.length > maxLengh){
            descripcion = descripcion.substring(0,maxLengh-3)+"..."
        } else{

        }
        if(!descripcion.equals("")){
            tvDescripcion?.text = descripcion
        } else{
            tvDescripcion?.setVisibility(View.GONE)
        }

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