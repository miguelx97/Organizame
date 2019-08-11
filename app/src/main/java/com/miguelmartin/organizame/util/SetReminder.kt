package com.miguelmartin.organizame.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.miguelmartin.organizame.bbdd.COL_ID
import com.miguelmartin.organizame.bbdd.DbPersistenciaTareas
import com.miguelmartin.organizame.model.Tarea
import java.util.*

class SetReminder{

    var context:Context?=null
    constructor(context:Context){
        this.context=context
    }

    fun setTime(){

        val tarea:Tarea? = DbPersistenciaTareas(context!!).getNextByFechaNotificacion()

        if (tarea != null){
            if(tarea.fechaNotificacion != null){
                val am= context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                var intent=Intent(context,BroadcastReceiver::class.java)
                intent.putExtra(COL_ID, tarea.id.toString())
                intent.action= SERVICE_REMINDER
                val pi=PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
//            Toast.makeText(context, tarea.fechaNotificacion.toString(), Toast.LENGTH_LONG).show()
                am.set(AlarmManager.RTC_WAKEUP,tarea.fechaNotificacion!!.getTime(), pi)
            }
        }
    }
}
