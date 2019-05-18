package com.miguelmartin.organizame

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.miguelmartin.organizame.entity.Tarea
import kotlinx.android.synthetic.main.activity_add_tarea.*
import kotlinx.android.synthetic.main.activity_add_tarea.toolbar
import java.util.*

class AddTareaActivity : AppCompatActivity() {


    var id:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tarea)
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        id=intent.getStringExtra("")


        if (id!=null){
            supportActionBar!!.title = "Nueva Tarea"
//            etTitulo.setText(intent.getStringExtra(""))
//            etDescripcion.setText(intent.getStringExtra(""))
        } else{
            supportActionBar!!.title = "Modificar Tarea"
        }

        btnAnadir.setOnClickListener {
            anadir()
        }
    }

    private fun anadir() {

        val titulo = etTitulo.text.toString();
        val descripcion = etDescripcion.text.toString();
        val fecha = Date()
        val tarea:Tarea = Tarea(0,titulo,descripcion,1,fecha);

        var values = ContentValues()
        values.put("", etTitulo.text.toString())
        values.put("", etDescripcion.text.toString())

        var res:Int = 1
        var action:String

        if (id==null){
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
