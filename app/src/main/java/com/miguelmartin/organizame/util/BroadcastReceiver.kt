package com.miguelmartin.organizame.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.miguelmartin.organizame.R
import com.miguelmartin.organizame.bbdd.COL_ID
import com.miguelmartin.organizame.bbdd.DbPersistenciaTareas
import com.miguelmartin.organizame.model.Tarea
import java.util.*


class BroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent!!.action == SERVICE_REMINDER){
            val b = intent.extras
            val id = b.getString(COL_ID)
            val tarea: Tarea = DbPersistenciaTareas(context).getItem(id)

            if(tarea.fechaNotificacion!!.equals(tarea.fecha)){
                Notifications(context).createNotification(tarea.titulo,tarea.descripcion)
            } else{
                Notifications(context).createNotification(tarea.titulo,formatoNotificacion.format(tarea.fecha))
            }

            if(!tarea.fechaNotificacion!!.equals(tarea.fecha) && !tarea.fechaNotificacion!!.after(Date())){
                DbPersistenciaTareas(context).cambiarFechaNotificacion(tarea, tarea.fecha!!)
            }
        }

        SetReminder(context).setTime()
    }

}
