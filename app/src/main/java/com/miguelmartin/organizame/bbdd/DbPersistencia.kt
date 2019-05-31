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
        val res = dbManager.insertar(getValues(tarea))

        return res
    }

    fun eliminar(tarea:Tarea){
        var dbManager= DbManager(this.context!!)
        val selectionArgs= arrayOf(tarea.id.toString())
        dbManager.eliminar("ID=?", selectionArgs)

    }

    fun modificar(tarea:Tarea):Int{

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


    val format = SimpleDateFormat("dd/MM/yyyy kk:mm")

    fun fechaToString(date: Date?) : String {
        var fecha:String
        if (date == null) fecha = ""
        else fecha = format.format(date)

        return fecha
    }

    fun stringToFecha(string:String?) : Date {
        var fecha:Date
        if (string == "") fecha = Date()
        else fecha = format.parse(string)
        return fecha
    }
}