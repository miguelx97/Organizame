package com.miguelmartin.organizame.bbdd

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder


class DbManager {

    val dbName= "Organizame"
    val dbVersion=1
    var currentTable:String? = null

    var sqlDB:SQLiteDatabase?=null

    constructor(context: Context, currentTable:String){
        val db=DatabaseHelper(context)
        sqlDB = db.writableDatabase
        this.currentTable = currentTable
    }

    inner class DatabaseHelper:SQLiteOpenHelper{
        var context: Context?=null
        constructor(context: Context) : super(  context,  dbName,  null, dbVersion ){
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTableTareas)
            db!!.execSQL(sqlCreateTableCategorias)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("Drop table IF EXISTS " + DB_TABLE_TAREAS)
            db!!.execSQL("Drop table IF EXISTS " + DB_TABLE_CATEGORIAS)
        }
    }

    fun insertar(values:ContentValues):Int{
        val id= sqlDB!!.insert(currentTable, "", values)
        return id.toInt()
    }

    fun customQuery(projection:Array<String>, selection:String, selectionArgs:Array<String>, sortOrder:String):Cursor{
        var qb=SQLiteQueryBuilder()
        qb.tables = currentTable

        val cursor = qb.query(sqlDB,projection,selection, selectionArgs, null,null,sortOrder)

        return cursor
    }

    fun eliminar(selection: String, selectionArgs: Array<String>):Int{
        val count=sqlDB!!.delete(currentTable, selection, selectionArgs)
        return count
    }

    fun modificar(values:ContentValues, selection: String, selectionArgs: Array<String>):Int{
        val count=sqlDB!!.update(currentTable, values, selection, selectionArgs)
        return count
    }

}