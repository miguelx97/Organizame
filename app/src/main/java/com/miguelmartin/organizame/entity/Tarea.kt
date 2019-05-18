package com.miguelmartin.organizame.entity

import java.util.*

const val NOMBRE_TABLA = "tarea"

data class Tarea(
    val id: Int,
    val titulo: String?,
    val descripcion: String?,
    val importancia: Int?,
    val fecha: Date?


)