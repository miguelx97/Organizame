package com.miguelmartin.organizame.data


import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.Toast
import com.miguelmartin.organizame.R
import com.miguelmartin.organizame.util.ESTADO_ARCHIVADO
import com.miguelmartin.organizame.util.ESTADO_ELIMINADO
import com.miguelmartin.organizame.activities.MainActivity
import com.miguelmartin.organizame.activities.MainActivity.Companion.estado
import com.miguelmartin.organizame.bbdd.DbPersistenciaTareas
import com.miguelmartin.organizame.model.Tarea
import com.miguelmartin.organizame.util.ESTADO_INICIAL
import com.miguelmartin.organizame.util.SetReminder

//https://www.youtube.com/watch?v=eEonjkmox-0&list=WL&index=23&t=1s
class SwipeActions(context: Context, dbPersistencia:DbPersistenciaTareas) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

            private lateinit var iconoSwipe: Drawable
            private lateinit var fondoSwipe: ColorDrawable
            val context = context
            val dbPersistencia:DbPersistenciaTareas = dbPersistencia
            var accion:String = ""

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if(direction == ItemTouchHelper.RIGHT)
                    when(estado){
                        ESTADO_INICIAL -> archivar(MainActivity.listaTareas.get(viewHolder.adapterPosition))
                        ESTADO_ARCHIVADO -> eliminar(MainActivity.listaTareas.get(viewHolder.adapterPosition), viewHolder)
                        ESTADO_ELIMINADO -> salvada(MainActivity.listaTareas.get(viewHolder.adapterPosition))
                    }

                else if(direction == ItemTouchHelper.LEFT)
                    when(estado){
                        ESTADO_INICIAL -> eliminar(MainActivity.listaTareas.get(viewHolder.adapterPosition), viewHolder)
                        ESTADO_ARCHIVADO -> salvada(MainActivity.listaTareas.get(viewHolder.adapterPosition))
                        ESTADO_ELIMINADO -> archivar(MainActivity.listaTareas.get(viewHolder.adapterPosition))
                    }

                SetReminder(context).setTime()
            }

            private fun eliminar(tarea: Tarea, viewHolder:RecyclerView.ViewHolder){
                val listaTareasPosicion:Int = MainActivity.listaTareas.indexOf(tarea)
                val listaTareasCompletaPosicion:Int = MainActivity.listaTareasCompleta.indexOf(tarea)
                var res = cambiarTareaDeEstado(tarea, ESTADO_ELIMINADO)
                accion = "eliminada"

                if (res > 0)
                    Snackbar.make(viewHolder.itemView, context.getResources().getString(R.string.nota_accion, accion), Snackbar.LENGTH_LONG).setAction("Deshacer") {
                        MainActivity.listaTareasCompleta.add(listaTareasCompletaPosicion, tarea)
                        MainActivity.listaTareas.add(listaTareasPosicion, tarea)
                        dbPersistencia.estadoInicial(tarea)
                        MainActivity.adapter.notifyItemInserted(listaTareasPosicion)
                    }.show()
                else
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()

            }

            private fun archivar(tarea: Tarea){
                val res = cambiarTareaDeEstado(tarea, ESTADO_ARCHIVADO)
                accion = "archivada"

                if (res > 0)
                    Toast.makeText(context, context.getResources().getString(R.string.nota_accion, accion), Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
            }

            private fun salvada(tarea: Tarea){
                val res = cambiarTareaDeEstado(tarea, ESTADO_INICIAL)
                accion = "reactivada"

                if (res > 0)
                    Toast.makeText(context, context.getResources().getString(R.string.nota_accion, accion), Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
            }

            private fun cambiarTareaDeEstado(tarea: Tarea, estado:Int): Int{
                MainActivity.listaTareasCompleta.remove(tarea)
                MainActivity.listaTareas.remove(tarea)
                MainActivity.adapter.notifyDataSetChanged()
                var res = dbPersistencia.cambiarEstado(tarea.id, estado)
                return res
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
        val itemView = viewHolder.itemView

        iconoSwipe = ContextCompat.getDrawable(MainActivity.context, R.drawable.inbox)!!
        fondoSwipe =   ColorDrawable(ContextCompat.getColor(MainActivity.context, R.color.colorInbox))

        val iconMarginVertical = (viewHolder.itemView.height - iconoSwipe.intrinsicHeight) / 2

        if (dX > 0) {
            fondoSwipe = ColorDrawable(ContextCompat.getColor(MainActivity.context, getColor(dX)))
            iconoSwipe = ContextCompat.getDrawable(MainActivity.context, getItem(dX))!!

            fondoSwipe.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
            iconoSwipe.setBounds(itemView.left + iconMarginVertical, itemView.top + iconMarginVertical,
                itemView.left + iconMarginVertical + iconoSwipe.intrinsicWidth, itemView.bottom - iconMarginVertical)
        } else {
            fondoSwipe =   ColorDrawable(ContextCompat.getColor(MainActivity.context, getColor(dX)))
            iconoSwipe = ContextCompat.getDrawable(MainActivity.context, getItem(dX))!!

            fondoSwipe.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            iconoSwipe.setBounds(itemView.right - iconMarginVertical - iconoSwipe.intrinsicWidth, itemView.top + iconMarginVertical,
                itemView.right - iconMarginVertical, itemView.bottom - iconMarginVertical)
            iconoSwipe.level = 0
        }

        fondoSwipe.draw(c)

        c.save()

        if (dX > 0)
            c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
        else
            c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)

        iconoSwipe.draw(c)

        c.restore()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun getColor(direction:Float): Int {
        var color = 0
        if (direction > 0)
            when (estado) {
                ESTADO_INICIAL -> color = R.color.colorArchivar
                ESTADO_ARCHIVADO -> color = R.color.colorBorrar
                ESTADO_ELIMINADO -> color = R.color.colorInbox
            }
        else
            when (estado) {
                ESTADO_INICIAL -> color = R.color.colorBorrar
                ESTADO_ARCHIVADO -> color = R.color.colorInbox
                ESTADO_ELIMINADO -> color = R.color.colorArchivar
            }
        return color
    }

    private fun getItem(direction:Float): Int {
        var item = 0
        if (direction > 0)
            when (estado) {
                ESTADO_INICIAL -> item = R.drawable.archivar
                ESTADO_ARCHIVADO -> item = R.drawable.papelera_blanca
                ESTADO_ELIMINADO -> item = R.drawable.inbox
            }
        else
            when (estado) {
                ESTADO_INICIAL -> item = R.drawable.papelera_blanca
                ESTADO_ARCHIVADO -> item = R.drawable.inbox
                ESTADO_ELIMINADO -> item = R.drawable.archivar
            }
        return item
    }

}