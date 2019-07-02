package com.miguelmartin.organizame.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.miguelmartin.organizame.R


class BroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent!!.action == SERVICE_REMINDER){
            val b = intent.extras
            val titulo = b.getString(NOTIFICACION_TITULO)
            val mensaje = b.getString(NOTIFICACION_MENSAJE)
            Notifications(context).createNotification(titulo,mensaje)
        }

        SetReminder(context).setTime()
    }

}
