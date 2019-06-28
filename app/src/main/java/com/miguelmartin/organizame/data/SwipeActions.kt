package com.miguelmartin.organizame.data


import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.Toast
import com.miguelmartin.organizame.R
import com.miguelmartin.organizame.Util.ESTADO_ARCHIVADO
import com.miguelmartin.organizame.Util.ESTADO_ELIMINADO
import com.miguelmartin.organizame.Util.ESTADO_INICIAL
import com.miguelmartin.organizame.activities.MainActivity
import com.miguelmartin.organizame.activities.MainActivity.Companion.estado
import com.miguelmartin.organizame.bbdd.DbPersistenciaTareas
import com.miguelmartin.organizame.model.Tarea

//https://www.youtube.com/watch?v=eEonjkmox-0&list=WL&index=23&t=1s
class SwipeActions(context: Context, dbPersistencia:DbPersistenciaTareas) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

            private lateinit var iconoSwipe: Drawable
            private lateinit var fondoSwipe: ColorDrawable
            val context = context
            val dbPersistencia:DbPersistenciaTareas = dbPersistencia

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if(direction == ItemTouchHelper.RIGHT){
                    eliminar(MainActivity.listaTareas.get(viewHolder.adapterPosition), viewHolder)
                }
                else if(direction == ItemTouchHelper.LEFT)
                    archivar(MainActivity.listaTareas.get(viewHolder.adapterPosition))

            }

            private fun eliminar(tarea: Tarea, viewHolder:RecyclerView.ViewHolder){
                val listaTareasPosicion:Int = MainActivity.listaTareas.indexOf(tarea)
                val listaTareasCompletaPosicion:Int = MainActivity.listaTareasCompleta.indexOf(tarea)
                MainActivity.listaTareasCompleta.remove(tarea)
                MainActivity.listaTareas.remove(tarea)
                MainActivity.adapter.notifyItemRemoved(listaTareasPosicion)
                val res = dbPersistencia.eliminar(tarea)
                if (res > 0)
                    Snackbar.make(viewHolder.itemView, context.getResources().getString(R.string.nota_eliminada), Snackbar.LENGTH_LONG).setAction("Desacer") {
                        MainActivity.listaTareasCompleta.add(listaTareasCompletaPosicion, tarea)
                        MainActivity.listaTareas.add(listaTareasPosicion, tarea)
                        dbPersistencia.estadoInicial(tarea)
                        MainActivity.adapter.notifyItemInserted(listaTareasPosicion)

                    }.show()
                else
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
            }

            private fun archivar(tarea: Tarea){
                MainActivity.listaTareasCompleta.remove(tarea)
                MainActivity.listaTareas.remove(tarea)
                MainActivity.adapter.notifyDataSetChanged()
                var res = 0
                if(MainActivity.estado == ESTADO_INICIAL){
                    res = dbPersistencia.archivar(tarea)
                } else if(MainActivity.estado == ESTADO_ARCHIVADO){
                    res = dbPersistencia.estadoInicial(tarea)
                }

                if (res > 0)
                    Toast.makeText(context, "Nota archivada", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()


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
                    if(estado != ESTADO_ELIMINADO){
                        fondoSwipe = ColorDrawable(ContextCompat.getColor(MainActivity.context, R.color.colorBorrar))
                        iconoSwipe = ContextCompat.getDrawable(MainActivity.context, R.drawable.papelera_blanca)!!
                    }


                    fondoSwipe.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                    iconoSwipe.setBounds(itemView.left + iconMarginVertical, itemView.top + iconMarginVertical,
                        itemView.left + iconMarginVertical + iconoSwipe.intrinsicWidth, itemView.bottom - iconMarginVertical)
                } else {
                    if(estado != ESTADO_ARCHIVADO){
                        iconoSwipe = ContextCompat.getDrawable(MainActivity.context, R.drawable.archivar)!!
                        fondoSwipe =   ColorDrawable(ContextCompat.getColor(MainActivity.context, R.color.colorArchivar))
                    }
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

}