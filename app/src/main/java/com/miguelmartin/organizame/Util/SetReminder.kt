package com.miguelmartin.organizame.Util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.miguelmartin.organizame.bbdd.DB_TABLE_TAREAS
import com.miguelmartin.organizame.bbdd.DbPersistenciaCategorias
import com.miguelmartin.organizame.bbdd.DbPersistenciaTareas
import com.miguelmartin.organizame.model.Tarea
import java.util.*

class SetReminder{

    var context:Context?=null
    constructor(context:Context){
        this.context=context
    }

    fun setTime(){

        val tarea:Tarea = DbPersistenciaTareas(context!!).getNextByFecha()

        if(tarea.fecha != null){
            val am= context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            var intent=Intent(context,BroadcastReceiver::class.java)
            intent.putExtra(NOTIFICACION_TITULO, tarea.titulo)
            intent.putExtra(NOTIFICACION_MENSAJE, tarea.descripcion)
            intent.action= SERVICE_REMINDER
            val pi=PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

            am.set(AlarmManager.RTC_WAKEUP,tarea.fecha!!.getTime(), pi)

//            am.setRepeating(AlarmManager.RTC_WAKEUP,tarea.fecha!!.getTime(),  AlarmManager.INTERVAL_DAY,pi)
        }

        
    }


}
