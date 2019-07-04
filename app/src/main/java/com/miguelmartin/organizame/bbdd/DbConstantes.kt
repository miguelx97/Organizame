package com.miguelmartin.organizame.bbdd

import com.miguelmartin.organizame.util.ESTADO_INICIAL

const val DB_TABLE_TAREAS= "tareas"
const val COL_ID= "id"
const val COL_TITULO= "titulo"
const val COL_DESCRIPCION= "descripcion"
const val COL_PRIORIDAD= "prioridad"
const val COL_ESTADO= "estado"
const val COL_FK_ID_CATEGORIA= "fk_id_categora"
const val COL_FECHA= "fecha"
const val COL_FECHA_CAMBIO_ESTADO= "fecha_cambio_estado"

const val DB_TABLE_CATEGORIAS= "categorias"
const val COL_ID_CATE= "id_cate"
const val COL_TITULO_CATE= "titulo_cate"
const val COL_COLOR_CATE= "color_cate"

const val sqlCreateTableTareas =
    "CREATE TABLE IF NOT EXISTS $DB_TABLE_TAREAS (" +
        " $COL_ID INTEGER PRIMARY KEY, " +
        " $COL_TITULO TEXT, " +
        " $COL_DESCRIPCION TEXT, " +
        " $COL_PRIORIDAD INTEGER, " +
        " $COL_FECHA TEXT, " +
        " $COL_FECHA_CAMBIO_ESTADO TEXT, " +
        " $COL_FK_ID_CATEGORIA INTEGER, " +
        " $COL_ESTADO INTEGER DEFAULT $ESTADO_INICIAL, " +
        " FOREIGN KEY($COL_FK_ID_CATEGORIA) REFERENCES $DB_TABLE_CATEGORIAS($COL_ID)" +
        " );"

const val sqlCreateTableCategorias=
    "CREATE TABLE IF NOT EXISTS $DB_TABLE_CATEGORIAS (" +
        " $COL_ID_CATE INTEGER PRIMARY KEY, " +
        " $COL_TITULO_CATE TEXT, " +
        " $COL_COLOR_CATE INTEGER " +
        " );"

const val QUERY_GET_NEXT_TAREAS_BY_FECHA =
    "SELECT $COL_ID, $COL_TITULO, $COL_DESCRIPCION, $COL_PRIORIDAD, $COL_FECHA" +
            " FROM $DB_TABLE_TAREAS" +
            " WHERE $COL_FECHA > ?" +
            " AND $COL_ESTADO = $ESTADO_INICIAL" +
            " ORDER BY $COL_FECHA"

const val QUERY_GET_TAREAS_BY_CATEGORIA = ""