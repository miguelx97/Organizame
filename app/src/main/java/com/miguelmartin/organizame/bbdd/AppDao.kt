package com.miguelmartin.organizame.bbdd

import android.arch.persistence.room.*
import com.miguelmartin.organizame.entity.Tarea

@Dao
interface AppDao : IDatabase {
    @Query("SELECT id,titulo,descripcion,importancia,fecha FROM tarea")
    override fun obtenerTodos(): List<Tarea>

    @Query("SELECT id, titulo, descripcion, importancia, fecha FROM tarea WHERE titulo LIKE :texto OR descripcion LIKE :texto")
    override fun obtener(texto: String): List<Tarea>

    @Insert
    override fun insertar(tarea: Tarea)

    @Delete
    override fun borrar(tarea: Tarea)

    @Update
    override fun modificar(tarea: Tarea)
}