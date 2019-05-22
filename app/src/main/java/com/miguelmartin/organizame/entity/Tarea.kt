package com.miguelmartin.organizame.entity

import java.io.Serializable
import java.util.*

const val NOMBRE_TABLA = "tarea"

class Tarea(
    var id: Int = 0,
    var titulo: String? = "",
    var descripcion: String? = "",
    var prioridad: Int? = 1,
    var fecha: Date? = null


): Serializable