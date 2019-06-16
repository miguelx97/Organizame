package com.miguelmartin.organizame.bbdd

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.miguelmartin.organizame.model.Categoria
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
        val cursor = dbManager.getDatos(projection, "$COL_TITULO like ? ", selectionArgs, COL_FECHA)

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

    fun eliminar(tarea:Tarea):Int{
        Log.w("eliminar tarea ${tarea.id}:", tarea.toString())
        val selectionArgs= arrayOf(tarea.id.toString())
        val res = dbManager.eliminar("$COL_ID=?", selectionArgs)
        return res
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
        values.put(COL_FK_ID_CATEGORIA, tarea.categoria.id)
        return values
    }

    fun getTaresByCategoria(arrCategorias: Array<String>):List<Tarea> {
        var list = ArrayList<Tarea>()
        var condiciones = ""
        if(!arrCategorias.isEmpty()){
            condiciones = " where 1=2"
            arrCategorias.forEach {
                condiciones += " or c.$COL_ID_CATE = ?"
            }
        }




        val query = "select t.$COL_ID, t.$COL_TITULO, t.$COL_DESCRIPCION, t.$COL_FECHA, t.$COL_PRIORIDAD, c.$COL_ID_CATE, c.$COL_TITULO_CATE, c.$COL_COLOR_CATE" +
                " from $DB_TABLE_TAREAS t" +
                " left join $DB_TABLE_CATEGORIAS c on" +
                " t.$COL_FK_ID_CATEGORIA = c.$COL_ID_CATE" +
                condiciones +
                " order by t.$COL_PRIORIDAD, t.$COL_FECHA"

        val cursor = dbManager.customQuery(query, arrCategorias)

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
                        stringToFecha(cursor.getString(cursor.getColumnIndex(COL_FECHA))),
                        Categoria(
                            cursor.getInt(cursor.getColumnIndex(COL_ID_CATE)),
                            cursor.getString(cursor.getColumnIndex(COL_TITULO_CATE)),
                            cursor.getInt(cursor.getColumnIndex(COL_COLOR_CATE))
                        )
                    )

                list.add(tarea)

                Log.w("get tarea ${tarea.id}:", tarea.toString())

            } while (cursor.moveToNext())
        }

        return list
    }

    fun getNextByFecha():Tarea {
        var projection = arrayOf(COL_ID, COL_TITULO, COL_DESCRIPCION, COL_PRIORIDAD, COL_FECHA)
        val selectionArgs= arrayOf(fechaToString(Date()))
        val query = "select $COL_ID, $COL_TITULO, $COL_DESCRIPCION, $COL_PRIORIDAD, $COL_FECHA from $DB_TABLE_TAREAS t where $COL_FECHA > ? order by $COL_FECHA"
        val cursor = dbManager.customQuery(query, selectionArgs)

        var tarea:Tarea = Tarea()

        if(cursor.moveToFirst()){
            tarea =
                Tarea(
                    cursor.getInt(cursor.getColumnIndex(COL_ID)),
                    cursor.getString(cursor.getColumnIndex(COL_TITULO)),
                    cursor.getString(cursor.getColumnIndex(COL_DESCRIPCION)),
                    cursor.getInt(cursor.getColumnIndex(COL_PRIORIDAD)),
                    stringToFecha(cursor.getString(cursor.getColumnIndex(COL_FECHA)))
                )
        }
        Log.w("get tarea ${tarea.id}:", tarea.toString())


        return tarea
    }


    val sinFecha = "sin fecha"
    val format = SimpleDateFormat("yyyyMMddkkmmss")
    fun fechaToString(date: Date?) : String {
        var fecha:String = sinFecha
        if (date != null) fecha = format.format(date)
        return fecha
    }

    fun stringToFecha(string:String?) : Date? {
        var fecha: Date? = null
        if (string != sinFecha) fecha = format.parse(string)
        return fecha
    }
}