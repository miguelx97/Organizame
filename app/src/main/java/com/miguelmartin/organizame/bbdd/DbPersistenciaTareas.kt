package com.miguelmartin.organizame.bbdd

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.miguelmartin.organizame.model.Tarea
import java.text.SimpleDateFormat
import java.util.*

class DbPersistenciaTareas {

    var context: Context
    var dbManager:DbManager

    constructor(context: Context){
        this.context = context
        dbManager = DbManager(context, DB_TABLE_TAREAS)
    }

    fun getItems(filtro:String):List<Tarea> {
        var list = ArrayList<Tarea>()
        var projection = arrayOf(COL_ID, COL_TITULO, COL_DESCRIPCION, COL_PRIORIDAD, COL_FECHA)
        val selectionArgs= arrayOf(filtro)
        val cursor = dbManager.customQuery(projection, "$COL_TITULO like ? ", selectionArgs, COL_FECHA)

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

                Log.w("get tarea ${tarea.id}:", tarea.toString())

            } while (cursor.moveToNext())
        }

        return list
    }

    fun insertar(tarea:Tarea):Int{
        Log.w("insertar tarea ${tarea.id}:", tarea.toString())
        val res = dbManager.insertar(getValues(tarea))

        return res
    }

    fun eliminar(tarea:Tarea){
        Log.w("eliminar tarea ${tarea.id}:", tarea.toString())
        val selectionArgs= arrayOf(tarea.id.toString())
        dbManager.eliminar("$COL_ID=?", selectionArgs)

    }

    fun modificar(tarea:Tarea):Int{
        Log.w("modificar tarea ${tarea.id}:", tarea.toString())
        var selectionArgs= arrayOf(tarea.id.toString())
        val res = dbManager.modificar(getValues(tarea), "$COL_ID=?", selectionArgs)

        return res
    }

    fun getValues(tarea:Tarea): ContentValues {
        var values = ContentValues()
        values.put(COL_TITULO, tarea.titulo)
        values.put(COL_DESCRIPCION, tarea.descripcion)
        values.put(COL_PRIORIDAD, tarea.prioridad)
        values.put(COL_FECHA, fechaToString(tarea.fecha))
        return values
    }

    val format = SimpleDateFormat("yyyyMMddkkmmss")
    fun fechaToString(date: Date?) : String {
        var fecha:String = ""
        if (date != null) fecha = format.format(date)
        return fecha
    }

    fun stringToFecha(string:String?) : Date? {
        var fecha: Date? = null
        if (string != "") fecha = format.parse(string)
        return fecha
    }
}