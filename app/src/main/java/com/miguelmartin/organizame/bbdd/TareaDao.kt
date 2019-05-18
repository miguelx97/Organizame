package com.miguelmartin.organizame.bbdd

import com.miguelmartin.organizame.entity.Tarea

interface TareaDao : IDatabase {

    override fun obtenerTodos(): List<Tarea>
    override fun obtener(texto: String): List<Tarea>
    override fun insertar(tarea: Tarea)
    override fun borrar(tarea: Tarea)
    override fun modificar(tarea: Tarea)
}