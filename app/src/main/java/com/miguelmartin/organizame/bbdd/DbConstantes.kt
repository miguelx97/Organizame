package com.miguelmartin.organizame.bbdd

const val DB_TABLE_TAREAS= "Tareas"
const val DB_TABLE_CATEGORIAS= "Categorias"
const val COL_ID= "Id"
const val COL_TITULO= "Titulo"
const val COL_DESCRIPCION= "Dascripcion"
const val COL_PRIORIDAD= "Prioridad"
const val COL_FK_ID_CATEGORIA= "fk_id_categora"
const val COL_FECHA= "Fecha"
const val COL_COLOR= "Color"

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
        " $COL_ID INTEGER PRIMARY KEY, " +
        " $COL_TITULO TEXT, " +
        " $COL_COLOR INTEGER " +
        " );"