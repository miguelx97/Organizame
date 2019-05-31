package com.miguelmartin.organizame

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.miguelmartin.organizame.bbdd.DB_TABLE
import com.miguelmartin.organizame.bbdd.DbPersistencia
import com.miguelmartin.organizame.constantes.formatoFecha
import com.miguelmartin.organizame.constantes.formatoHora
import com.miguelmartin.organizame.entity.Tarea
import kotlinx.android.synthetic.main.activity_add_tarea.*
import kotlinx.android.synthetic.main.activity_add_tarea.toolbar
import kotlinx.android.synthetic.main.activity_add_tarea.tvFecha
import kotlinx.android.synthetic.main.activity_add_tarea.tvHora
import kotlinx.android.synthetic.main.activity_detalle.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class DetalleActivity : AppCompatActivity() {

    var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Tarea"

        var tarea = intent.getSerializableExtra(DB_TABLE) as Tarea

        tvTitulo.setText(tarea.titulo)
        tvDescripcion.setText(tarea.descripcion)
//            if (tarea.prioridad == 2) {
//                swPrioritario.setChecked(true)
//            }
        tvHora.text = formatoHora.format(tarea.fecha)
        tvFecha.text = formatoFecha.format(tarea.fecha)

        btnEliminar.setOnClickListener{
            val dbPersistencia = DbPersistencia(this)
            dbPersistencia.eliminar(tarea)
            Toast.makeText(this, "La tarea ha sido eliminada", Toast.LENGTH_LONG).show()
            finish()
        }

        btnEditar.setOnClickListener {
            var intent = Intent(this, AddTareaActivity::class.java)
            intent.putExtra(DB_TABLE, tarea)
            this.startActivity(intent)
            finish()
        }

    }
}
