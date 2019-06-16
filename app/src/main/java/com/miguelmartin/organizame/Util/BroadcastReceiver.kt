package com.miguelmartin.organizame.Util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.miguelmartin.organizame.bbdd.DB_TABLE_TAREAS
import com.miguelmartin.organizame.model.Tarea


class BroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent!!.action.equals(SERVICE_REMINDER)){
            val b = intent.extras
            val titulo = b.getString(NOTIFICACION_TITULO)
            val mensaje = b.getString(NOTIFICACION_MENSAJE)
//            Toast.makeText(context,b.getString("message"),Toast.LENGTH_LONG).show()
//            Toast.makeText(context,"funciona",Toast.LENGTH_LONG).show()
            Notifications(context!!).createNotification(titulo!!,mensaje)
        }

        SetReminder(context!!).setTime()
    }

}
