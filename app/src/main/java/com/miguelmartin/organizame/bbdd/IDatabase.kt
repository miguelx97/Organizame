package com.miguelmartin.organizame.bbdd

import com.miguelmartin.organizame.entity.Tarea

interface IDatabase {
    fun obtenerTodos(): List<Tarea>
    fun obtener(texto:String): List<Tarea>
    fun insertar(tarea: Tarea)
    fun modificar(tarea: Tarea)
    fun borrar(tarea: Tarea)
}