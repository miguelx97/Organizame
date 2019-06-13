package com.miguelmartin.organizame.bbdd

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.miguelmartin.organizame.model.Categoria
import java.util.*

class DbPersistenciaCategorias {

    var context: Context
    var dbManager:DbManager

    constructor(context: Context){
        this.context = context
        dbManager = DbManager(context, DB_TABLE_CATEGORIAS)
    }

    fun getItems(filtro:String):List<Categoria> {
        var list = ArrayList<Categoria>()
        var projection = arrayOf(COL_ID_CATE, COL_TITULO_CATE, COL_COLOR_CATE)
        val selectionArgs= arrayOf(filtro)
        val cursor = dbManager.getDatos(projection, "$COL_TITULO_CATE like ? ", selectionArgs, COL_ID_CATE)

        list.clear()

        var categoria:Categoria

        if(cursor.moveToFirst()){
            do {

                categoria =
                    Categoria(
                        cursor.getInt(cursor.getColumnIndex(COL_ID_CATE)),
                        cursor.getString(cursor.getColumnIndex(COL_TITULO_CATE)),
                        cursor.getInt(cursor.getColumnIndex(COL_COLOR_CATE))
                    )

                list.add(categoria)

                Log.w("get categoria ${categoria.id}:", categoria.toString())

            } while (cursor.moveToNext())
        }

        return list
    }

    fun insertar(categoria:Categoria):Int{
        Log.w("insertar categoria ${categoria.id}:", categoria.toString())
        val res = dbManager.insertar(getValues(categoria))

        return res
    }

    fun eliminar(categoria:Categoria):Int{
        Log.w("eliminar categoria ${categoria.id}:", categoria.toString())
        val selectionArgs= arrayOf(categoria.id.toString())
        val res = dbManager.eliminar("$COL_ID_CATE=?", selectionArgs)

        return res

    }

    fun modificar(categoria:Categoria):Int{
        Log.w("modificar categoria ${categoria.id}:", categoria.toString())
        var selectionArgs= arrayOf(categoria.id.toString())
        val res = dbManager.modificar(getValues(categoria), "$COL_ID_CATE=?", selectionArgs)

        return res
    }

    fun getValues(categoria:Categoria): ContentValues {
        var values = ContentValues()
        values.put(COL_TITULO_CATE, categoria.titulo)
        values.put(COL_COLOR_CATE, categoria.color)
        return values
    }

}


