package com.miguelmartin.organizame.util

import java.text.SimpleDateFormat
import java.util.*

// FECHA Y HORA
val idioma = Locale("es", "ES")
val formatoFecha = SimpleDateFormat("EEE dd MMM", idioma)
val formatoHora = SimpleDateFormat("kk:mm", idioma)
val formatoNotificacion = SimpleDateFormat("'El' EEEE dd 'de' MMMM 'a las' kk:mm", idioma)
val formatoSegundos = SimpleDateFormat("ss")

// SERVICIO DE RECORDATORIO
val SERVICE_REMINDER = "com.miguelmartin.organizame.reminder"
val NOTIFICACION_TITULO = "notifTitulo"
val NOTIFICACION_MENSAJE = "notifMensaje"

//ESTADOS
const val ESTADO = 0
const val ESTADO_INICIAL = 1
const val ESTADO_ARCHIVADO = 2
const val ESTADO_ELIMINADO = 3

//CATEGORIAS
const val SIN_CATEGORIA = -1

//INTENTS
val CLASE = "nombre_de_clase"

//SHARE PREFERENCES
const val SHARE_PREFERENCES = "myPreferences"