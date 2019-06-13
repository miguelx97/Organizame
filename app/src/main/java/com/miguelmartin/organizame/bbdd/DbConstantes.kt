package com.miguelmartin.organizame.bbdd

const val DB_TABLE_TAREAS= "tareas"
const val COL_ID= "id"
const val COL_TITULO= "titulo"
const val COL_DESCRIPCION= "descripcion"
const val COL_PRIORIDAD= "prioridad"
const val COL_FK_ID_CATEGORIA= "fk_id_categora"
const val COL_FECHA= "fecha"

const val DB_TABLE_CATEGORIAS= "categorias"
const val COL_ID_CATE= "id_cate"
const val COL_TITULO_CATE= "titulo_cate"
const val COL_COLOR_CATE= "color_cate"

const val sqlCreateTableTareas= "CREATE TABLE IF NOT EXISTS $DB_TABLE_TAREAS (" +
        " $COL_ID INTEGER PRIMARY KEY, " +
        " $COL_TITULO TEXT, " +
        " $COL_DESCRIPCION TEXT, " +
        " $COL_PRIORIDAD INTEGER, " +
        " $COL_FECHA TEXT, " +
        " $COL_FK_ID_CATEGORIA INTEGER, " +
        " FOREIGN KEY($COL_FK_ID_CATEGORIA) REFERENCES $DB_TABLE_CATEGORIAS($COL_ID)" +
        " );"

const val sqlCreateTableCategorias= "CREATE TABLE IF NOT EXISTS $DB_TABLE_CATEGORIAS (" +
        " $COL_ID_CATE INTEGER PRIMARY KEY, " +
        " $COL_TITULO_CATE TEXT, " +
        " $COL_COLOR_CATE INTEGER " +
        " );"