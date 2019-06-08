package com.miguelmartin.organizame.data

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import com.miguelmartin.organizame.R
import com.miguelmartin.organizame.activities.GestionCategoriasActivity
import com.miguelmartin.organizame.bbdd.DbPersistenciaCategorias
import com.miguelmartin.organizame.model.Categoria
import kotlinx.android.synthetic.main.item_categoria.view.*

class AppAdapterCategorias(private val list: ArrayList<Categoria>)
    : RecyclerView.Adapter<AppAdapterCategorias.AppViewHolderCategorias>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolderCategorias {
        val inflater = LayoutInflater.from(parent.context)
        return AppViewHolderCategorias(inflater, parent)
    }

    override fun onBindViewHolder(holder: AppViewHolderCategorias, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size


    inner class AppViewHolderCategorias(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_categoria, parent, false)) {

        lateinit var dbPersistencia: DbPersistenciaCategorias;

        private var tvTitulo: TextView? = null
        private var lyCategoria: LinearLayout? = null
        private var ivEditar: ImageView? = null
        private var ivEliminar: ImageView? = null

        fun bind(categoria: Categoria) {

            tvTitulo = itemView.findViewById(R.id.tvTitulo)
            lyCategoria = itemView.findViewById(R.id.lyCategoria)
            ivEditar = itemView.findViewById(R.id.ivEditar)
            ivEliminar = itemView.findViewById(R.id.ivEliminar)


            itemView.setBackgroundColor(categoria.color!!)

            tvTitulo?.text = categoria.titulo

            itemView.ivEliminar.setOnClickListener {ocEliminar(categoria)}
            itemView.ivEditar.setOnClickListener {}

        }

        fun ocEliminar(categoria: Categoria){
            val pos = adapterPosition
            dbPersistencia = DbPersistenciaCategorias(itemView.context)
            var r = dbPersistencia.eliminar(categoria)

            if(r > 0) {
                Toast.makeText(itemView.context, "Categoría eliminada", Toast.LENGTH_SHORT).show()
                list.removeAt(pos)
                notifyItemRemoved(pos)
            }
            else
                Toast.makeText(itemView.context,"Error", Toast.LENGTH_SHORT).show()

        }

        fun ocEditar(categoria: Categoria){
            val gestionCategoriasActivity = GestionCategoriasActivity()
            gestionCategoriasActivity.setDatos(categoria)
        }
    }

}