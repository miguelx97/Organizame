package com.miguelmartin.organizame.bbdd

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.miguelmartin.organizame.entity.Tarea
import java.text.SimpleDateFormat
import java.util.*

class DbPersistencia {

    var context: Context
    var dbManager:DbManager

    constructor(context: Context){
        this.context = context
        dbManager = DbManager(context)
    }

    fun getTodos():List<Tarea> {
        var list = ArrayList<Tarea>()
        var projection = arrayOf(COL_ID, COL_TITULO, COL_DESCRIPCION, COL_PRIORIDAD, COL_FECHA)
        val selectionArgs= arrayOf("%")
        val cursor = dbManager.customQuery(projection, "$COL_TITULO like ? ", selectionArgs, COL_TITULO)

        list.clear()

        var tarea:Tarea

        if(cursor.moveToFirst()){
            do {

                tarea =
                    Tarea(
                        cursor.getInt(cursor.getColumnIndex(COL_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_TITULO)),
                        cursor.getString(cursor.getColumnIndex(COL_DESCRIPCION)),
                        cursor.getInt(cursor.getColumnIndex(COL_PRIORIDAD)),
                        stringToFecha(cursor.getString(cursor.getColumnIndex(COL_FECHA)))
                    )

                list.add(tarea)

                Log.w("tarea:", tarea.toString())

            } while (cursor.moveToNext())
        }

        return list
    }

    fun insertar(tarea:Tarea):Int{
        var values = ContentValues()
        values.put(COL_TITULO, tarea.titulo)
        values.put(COL_DESCRIPCION, tarea.descripcion)
        values.put(COL_PRIORIDAD, tarea.prioridad)
        values.put(COL_FECHA, fechaToString(tarea.fecha))

        val res = dbManager.insertar(values)

        return res
    }

    fun eliminar(tarea:Tarea){
        var dbManager= DbManager(this.context!!)
        val selectionArgs= arrayOf(tarea.id.toString())
        dbManager.eliminar("ID=?", selectionArgs)

    }




    val format = SimpleDateFormat("dd/MM/yyyy hh:mm")

    fun fechaToString(date: Date?) : String {
        return format.format(date)
    }
    fun stringToFecha(string:String?) : Date {
        return format.parse(string)

    }
}