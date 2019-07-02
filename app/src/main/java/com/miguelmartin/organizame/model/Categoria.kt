package com.miguelmartin.organizame.model

import android.graphics.Color
import java.io.Serializable

class Categoria(
    var id: Int = 0,
    var titulo: String? = "",
    var color: Int? = Color.WHITE

): Serializable {
    override fun toString(): String {
        return "Categoria(id=$id, titulo=$titulo, color=$color)"
    }

    constructor(categoria: Categoria) : this() {
        this.id = categoria.id
        this.titulo = categoria.titulo
        this.color = categoria.color
    }
}