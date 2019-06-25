package com.miguelmartin.organizame.model

import com.miguelmartin.organizame.Util.ESTADO_INICIAL
import com.miguelmartin.organizame.activities.NO_IMPORTANTE
import java.io.Serializable
import java.util.*

class Tarea(
    var id: Int = 0,
    var titulo: String? = "",
    var descripcion: String? = "",
    var prioridad: Int? = NO_IMPORTANTE,
    var fecha: Date? = null,
    var categoria: Categoria = Categoria(),
    var estado: Int = ESTADO_INICIAL

): Serializable {

    override fun toString(): String {
        return "Tarea(id=$id, titulo=$titulo, descripcion=$descripcion, prioridad=$prioridad, fecha=$fecha, categoria=$categoria)"
    }
}