package com.miguelmartin.organizame.bbdd

import com.miguelmartin.organizame.entity.Tarea

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.miguelmartin.organizame.util.ConversoresFechas

/**
 * Created by Admin on 7/1/2017.
 */

@Database(entities = arrayOf(Tarea::class), version = 1, exportSchema = false)
@TypeConverters(ConversoresFechas::class)
abstract class AppDatabaseCon: RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        private const val DATABASE_NAME = "tareas_database"
        @Volatile
        private var INSTANCE: AppDatabaseCon? = null

        fun getInstance(context: Context): AppDatabaseCon? {
            INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabaseCon::class.java,
                    DATABASE_NAME
                ).build()
            }
            return INSTANCE
        }
    }

}
