package com.miguelmartin.organizame.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
data class Tarea(
    @PrimaryKey val id: Int,
    @ColumnInfo val titulo: String?,
    @ColumnInfo val descripcion: String?,
    @ColumnInfo val importancia: Int?,
    @ColumnInfo val fecha: Date?
)