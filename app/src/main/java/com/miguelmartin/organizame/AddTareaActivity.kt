package com.miguelmartin.organizame

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.miguelmartin.organizame.bbdd.DB_TABLE
import com.miguelmartin.organizame.bbdd.DbPersistencia
import com.miguelmartin.organizame.entity.Tarea
import kotlinx.android.synthetic.main.activity_add_tarea.*
import kotlinx.android.synthetic.main.activity_add_tarea.toolbar
import java.util.*

class AddTareaActivity : AppCompatActivity() {


    var id:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tarea)
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.getSerializableExtra(DB_TABLE) != null){
            var tarea = intent.getSerializableExtra(DB_TABLE) as Tarea
            supportActionBar!!.title = "Modificar tarea"
            etTitulo.setText(tarea.titulo)
            etDescripcion.setText(tarea.descripcion)
            if(tarea.prioridad == 2){
                swPrioritario.setChecked(true)
            }

        } else{
            supportActionBar!!.title = "Nueva Tarea"
        }

        btnAnadir.setOnClickListener {
            anadir()
        }
    }

    private fun anadir() {

        var tarea = Tarea()
        tarea.titulo = etTitulo.text.toString()
        tarea.descripcion = etDescripcion.text.toString()
        tarea.fecha = Date()

        if (swPrioritario.isChecked){
            tarea.prioridad=2
        }

        val titulo = etTitulo.text.toString();
        val descripcion = etDescripcion.text.toString();
        val fecha = Date()

//        val prioritario = swPrioritario.

//        val tarea:Tarea = Tarea(0,titulo,descripcion,1,fecha);

        var dbPersistencia = DbPersistencia(this)


        var res:Int = 0
        var action:String

        if (id==null){
            res = dbPersistencia.insertar(tarea)
            action = "añadida"
        } else{
            action = "modificada"
        }

        if (res > 0) {
            Toast.makeText(this, "la tarea ha sido $action", Toast.LENGTH_LONG).show()

            finish()
        } else {
            Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
        }
    }

}
