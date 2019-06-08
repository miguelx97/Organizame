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
        var projection = arrayOf(COL_ID, COL_TITULO, COL_COLOR)
        val selectionArgs= arrayOf(filtro)
        val cursor = dbManager.customQuery(projection, "$COL_TITULO like ? ", selectionArgs, COL_ID)

        list.clear()

        var categoria:Categoria

        if(cursor.moveToFirst()){
            do {

                categoria =
                    Categoria(
                        cursor.getInt(cursor.getColumnIndex(COL_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_TITULO)),
                        cursor.getInt(cursor.getColumnIndex(COL_COLOR))
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
        val res = dbManager.eliminar("$COL_ID=?", selectionArgs)

        return res

    }

    fun modificar(categoria:Categoria):Int{
        Log.w("modificar categoria ${categoria.id}:", categoria.toString())
        var selectionArgs= arrayOf(categoria.id.toString())
        val res = dbManager.modificar(getValues(categoria), "$COL_ID=?", selectionArgs)

        return res
    }

    fun getValues(categoria:Categoria): ContentValues {
        var values = ContentValues()
        values.put(COL_TITULO, categoria.titulo)
        values.put(COL_COLOR, categoria.color)
        return values
    }

}


