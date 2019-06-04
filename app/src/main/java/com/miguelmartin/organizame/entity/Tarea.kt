package com.miguelmartin.organizame.entity

import java.io.Serializable
import java.util.*

class Tarea(
    var id: Int = 0,
    var titulo: String? = "",
    var descripcion: String? = "",
    var prioridad: Int? = 1,
    var fecha: Date? = null


): Serializable {

    override fun toString(): String {
        return "Tarea(id=$id, titulo=$titulo, descripcion=$descripcion, prioridad=$prioridad, fecha=$fecha)"
    }
}