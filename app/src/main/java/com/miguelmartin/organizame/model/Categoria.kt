package com.miguelmartin.organizame.model

import java.io.Serializable

class Categoria(
    var id: Int = 0,
    var titulo: String? = "",
    var color: Int? = 0
): Serializable {
    override fun toString(): String {
        return "Categoria(id=$id, titulo=$titulo, color=$color)"
    }
}