package com.miguelmartin.organizame.bbdd

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.miguelmartin.organizame.model.Categoria
import com.miguelmartin.organizame.model.Tarea
import com.miguelmartin.organizame.util.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList



class DbPersistenciaTareas {

    var context: Context
    var dbManager:DbManager

    constructor(context: Context){
        this.context = context
        dbManager = DbManager(context, DB_TABLE_TAREAS)
    }

    fun getItem(filtro:String):Tarea{
        var projection = arrayOf(COL_ID, COL_TITULO, COL_DESCRIPCION, COL_PRIORIDAD, COL_FECHA, COL_FECHA_NOTIFICACION)
        val selectionArgs= arrayOf(filtro)
        val cursor = dbManager.getDatos(projection, "$COL_ID = ? ", selectionArgs, COL_ID)

        var tarea:Tarea? = null

        if(cursor.moveToFirst()){
                tarea =
                    Tarea(
                        cursor.getInt(cursor.getColumnIndex(COL_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_TITULO)),
                        cursor.getString(cursor.getColumnIndex(COL_DESCRIPCION)),
                        cursor.getInt(cursor.getColumnIndex(COL_PRIORIDAD)),
                        stringToFecha(cursor.getString(cursor.getColumnIndex(COL_FECHA))),
                        Categoria(),
                        ESTADO,
                        stringToFecha(cursor.getString(cursor.getColumnIndex(COL_FECHA_NOTIFICACION)))
                    )

                Log.w("get tarea ${tarea.id}:", tarea.toString())
        }

        return tarea!!
    }

    fun insertar(tarea:Tarea):Int{
        Log.w("insertar tarea ${tarea.id}:", tarea.toString())
        val res = dbManager.insertar(getValues(tarea))

        return res
    }

    fun eliminar(tarea:Tarea):Int{

        return cambiarEstado(tarea.id, ESTADO_ELIMINADO)
    }

    fun archivar(tarea:Tarea):Int{
        return cambiarEstado(tarea.id, ESTADO_ARCHIVADO)
    }

    fun estadoInicial(tarea:Tarea):Int{
        return cambiarEstado(tarea.id, ESTADO_INICIAL)
    }

    fun cambiarEstado(id:Int, estado:Int):Int{
        Log.w("Estado de $id a", estado.toString())
        var selectionArgs= arrayOf(id.toString())
        val cv = ContentValues()
        cv.put(COL_ESTADO, estado)
        cv.put(COL_FECHA_CAMBIO_ESTADO, fechaToString(Date()))

        val res = dbManager.modificar(cv, "$COL_ID=?", selectionArgs)

        return res
    }

    fun eliminarDefinitivoHaceUnDia():Int{
        val cal:Calendar = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
//        cal.add(Calendar.MINUTE, -2)
        val selectionArgs= arrayOf(fechaToString(cal.getTime()))
        val res = dbManager.eliminar("$COL_FECHA_CAMBIO_ESTADO < ? AND $COL_ESTADO = $ESTADO_ELIMINADO", selectionArgs)
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
        values.put(COL_FECHA_NOTIFICACION, fechaToString(tarea.fechaNotificacion))

        return values
    }

    fun getTaresByCategoria(categoria: Int, ver:Int):ArrayList<Tarea> {
        var list = ArrayList<Tarea>()
        var condiciones = ""

        if(categoria != SIN_CATEGORIA){
            condiciones += " and c.$COL_ID_CATE = $categoria"
        }
        if(ver != 0){
            condiciones += " and t.$COL_ESTADO = $ver"
        }

        val query = "select t.$COL_ID, t.$COL_TITULO, t.$COL_DESCRIPCION, t.$COL_FECHA, t.$COL_PRIORIDAD, t.$COL_ESTADO, t.$COL_FECHA_NOTIFICACION, c.$COL_ID_CATE, c.$COL_TITULO_CATE, c.$COL_COLOR_CATE" +
                " from $DB_TABLE_TAREAS t" +
                " left join $DB_TABLE_CATEGORIAS c on" +
                " t.$COL_FK_ID_CATEGORIA = c.$COL_ID_CATE" +
                " where 1=1" +
                condiciones +
                " order by t.$COL_PRIORIDAD, t.$COL_FECHA, t.$COL_TITULO"


        val cursor = dbManager.customQuery(query, emptyArray())

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
                        ),
                        cursor.getInt(cursor.getColumnIndex(COL_ESTADO)),
                        stringToFecha(cursor.getString(cursor.getColumnIndex(COL_FECHA_NOTIFICACION)))
                    )

                list.add(tarea)

                Log.w("get tarea ${tarea.id}:", tarea.toString())

            } while (cursor.moveToNext())
        }

        return list
    }

    fun getNextByFechaNotificacion():Tarea? {
        val selectionArgs= arrayOf(fechaToString(Date()))
        val query = QUERY_GET_NEXT_TAREAS_BY_FECHA_NOTIFICACION
        val cursor = dbManager.customQuery(query, selectionArgs)

        var tarea:Tarea? = null

        if(cursor.moveToFirst()){
            tarea =
                Tarea(
                    cursor.getInt(cursor.getColumnIndex(COL_ID)),
                    cursor.getString(cursor.getColumnIndex(COL_TITULO)),
                    cursor.getString(cursor.getColumnIndex(COL_DESCRIPCION)),
                    cursor.getInt(cursor.getColumnIndex(COL_PRIORIDAD)),
                    stringToFecha(cursor.getString(cursor.getColumnIndex(COL_FECHA))),
                    Categoria(),
                    ESTADO,
                    stringToFecha(cursor.getString(cursor.getColumnIndex(COL_FECHA_NOTIFICACION)))
                )
            Log.w("getNextByFechaNotificacion ${tarea.id}:", tarea.toString())
        }

        return tarea
    }

    fun cambiarFechaNotificacion(tarea:Tarea, nuevaFecha:Date):Int{
        Log.w("FechaNotificacion de ${tarea.id} a", nuevaFecha.toString())
        var selectionArgs= arrayOf(tarea.id.toString())
        val cv = ContentValues()
        cv.put(COL_FECHA_NOTIFICACION, fechaToString(nuevaFecha))
        val res = dbManager.modificar(cv, "$COL_ID=?", selectionArgs)

        return res
    }


    val sinFecha = "n/a"
    val format = SimpleDateFormat("yyyyMMddkkmmss")
    fun fechaToString(date: Date?) : String {
        var fecha:String = sinFecha
        if (date != null) fecha = format.format(date)
        return fecha
    }

    fun stringToFecha(string:String?) : Date? {
        var string:String? = string
        var fecha: Date? = null
        if("sin fecha".equals(string) || null == string) string = sinFecha  //TEMPORAL
        if (string != sinFecha) fecha = format.parse(string)
        return fecha
    }

}