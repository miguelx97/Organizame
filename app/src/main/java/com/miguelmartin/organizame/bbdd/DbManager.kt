package com.miguelmartin.organizame.bbdd

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.util.Log
import android.widget.Toast

const val DB_TABLE= "Tareas"
const val COL_TITULO= "Titulo"
const val COL_DESCRIPCION= "Dascripcion"
const val COL_PRIORIDAD= "Prioridad"
const val COL_FECHA= "Fecha"
const val COL_ID= "Id"

class DbManager {       //TODO: meter las columnas con sus tipos en un map para automatizar la clase

    val dbName= "MyNotes"
    val dbVersion=1
    val sqlCreateTable= "CREATE TABLE IF NOT EXISTS $DB_TABLE (" +
            " $COL_ID INTEGER PRIMARY KEY, " +
            " $COL_TITULO TEXT, " +
            " $COL_DESCRIPCION TEXT, " +
            " $COL_PRIORIDAD INTEGER, " +
            " $COL_FECHA TEXT " +
            " );"
    var sqlDB:SQLiteDatabase?=null

    constructor(context: Context){
        val db=DatabaseHelper(context)
        sqlDB = db.writableDatabase
    }



    inner class DatabaseHelper:SQLiteOpenHelper{
        var context: Context?=null
        constructor(context: Context) : super(  context,  dbName,  null, dbVersion ){
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context, " database is created", Toast.LENGTH_LONG).show()

        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("Drop table IF EXISTS " + DB_TABLE)
        }
    }

    fun insertar(values:ContentValues):Int{
        Log.d("QUERY", sqlCreateTable)
        val id= sqlDB!!.insert(DB_TABLE, "", values)
        return id.toInt()
    }

    fun customQuery(projection:Array<String>, selection:String, selectionArgs:Array<String>, sorOrder:String):Cursor{
        var qb=SQLiteQueryBuilder()
        qb.tables = DB_TABLE

        val cursor = qb.query(sqlDB,projection,selection, selectionArgs, null,null,sorOrder)

        return cursor
    }

    fun eliminar(selection: String, selectionArgs: Array<String>):Int{
        val count=sqlDB!!.delete(DB_TABLE, selection, selectionArgs)
        return count
    }

    fun modificar(values:ContentValues, selection: String, selectionArgs: Array<String>):Int{
        val count=sqlDB!!.update(DB_TABLE, values, selection, selectionArgs)
        return count
    }

}