package com.miguelmartin.organizame

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.Toast
import com.miguelmartin.organizame.bbdd.DB_TABLE
import com.miguelmartin.organizame.bbdd.DbPersistencia
import com.miguelmartin.organizame.constantes.formatoFecha
import com.miguelmartin.organizame.constantes.formatoHora
import com.miguelmartin.organizame.entity.Tarea
import kotlinx.android.synthetic.main.activity_add_tarea.*
import kotlinx.android.synthetic.main.activity_add_tarea.toolbar
import java.text.SimpleDateFormat
import java.util.*
import android.view.View


class AddTareaActivity : AppCompatActivity() {

    var id:Int? = null
    var cal = Calendar.getInstance()
    var tarea = Tarea()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tarea)
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        if (intent.getSerializableExtra(DB_TABLE) != null){     //MODIFICAR
            tarea = intent.getSerializableExtra(DB_TABLE) as Tarea
            supportActionBar!!.title = "Modificar tarea"
            btnAnadir.setText("Modificar")
            etTitulo.setText(tarea.titulo)
            etDescripcion.setText(tarea.descripcion)
            tvFecha.setText(formatoFecha.format(tarea.fecha))
            tvHora.setText(formatoHora.format(tarea.fecha))
            if(tarea.prioridad == 2){
                swPrioritario.setChecked(true)
            }
            id=tarea.id

        } else{                                                 //NUEVA
            supportActionBar!!.title = "Nueva Tarea"
            tvHora.text = formatoHora.format(cal.time)
            tvFecha.text = formatoFecha.format(cal.time)
        }

        btnAnadir.setOnClickListener {
            anadirModificar()
        }

        lyFecha.setOnClickListener(){
            ivCalendarioOnClick()
        }

        lyHora.setOnClickListener(){
            ivRelojOnClick()
        }

        var visible = true
        var view = 0

        btnFechaHora.setOnClickListener {
            if(visible){
                visible = false
                view = View.GONE
                btnFechaHora.setText("Añadir fecha y hora")
            } else{
                visible = true
                view = View.VISIBLE
                btnFechaHora.setText("Eliminar fecha y hora")
            }
            lyFechaHora.setVisibility(view)
        }

    }



    private fun anadirModificar() {

        tarea.titulo = etTitulo.text.toString()
        tarea.descripcion = etDescripcion.text.toString()
        tarea.fecha = cal.getTime()

        if (swPrioritario.isChecked){
            tarea.prioridad=2
        }


        val dbPersistencia = DbPersistencia(this)


        var res:Int = 0
        var action:String

        if (id==null){
            res = dbPersistencia.insertar(tarea)
            action = "añadida"
        } else{
            res = dbPersistencia.modificar(tarea)
            action = "modificada"
        }

        if (res > 0) {
            Toast.makeText(this, "la tarea ha sido $action", Toast.LENGTH_LONG).show()
            finish()

        } else {
            Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
        }
    }



    private fun ivRelojOnClick() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            tvHora.text = formatoHora.format(cal.time)
        }

        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    private fun ivCalendarioOnClick() {
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDay ->
            cal.set(Calendar.MONTH, mMonth)
            cal.set(Calendar.DAY_OF_MONTH, mDay)
            tvFecha.text = formatoFecha.format(cal.time)
        },year,month,day)

        datePickerDialog.show()
    }


}
