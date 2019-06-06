package com.miguelmartin.organizame.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.miguelmartin.organizame.bbdd.DbPersistenciaTareas
import com.miguelmartin.organizame.constantes.formatoFecha
import com.miguelmartin.organizame.constantes.formatoHora
import com.miguelmartin.organizame.model.Tarea
import kotlinx.android.synthetic.main.activity_add_tarea.*
import kotlinx.android.synthetic.main.activity_add_tarea.toolbar
import java.util.*
import android.view.View
import com.miguelmartin.organizame.R
import com.miguelmartin.organizame.bbdd.DB_TABLE_TAREAS


class AddTareaActivity : AppCompatActivity() {

    var id:Int? = null
    var cal = Calendar.getInstance()
    var tarea = Tarea()
    var fechaVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tarea)
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        if (intent.getSerializableExtra(DB_TABLE_TAREAS) != null){     //MODIFICAR
            tarea = intent.getSerializableExtra(DB_TABLE_TAREAS) as Tarea
            supportActionBar!!.title = "Modificar tarea"
            btnAnadir.setText("Modificar")
            etTitulo.setText(tarea.titulo)
            etDescripcion.setText(tarea.descripcion)

            if(tarea.fecha != null) {
                tvFecha.text = formatoFecha.format(tarea.fecha)
                if((tarea.fecha)!!.seconds != 0){
                    tvHora.text = formatoHora.format(tarea.fecha)
                } else{
                    tvHora.text = getString(R.string.escoge_hora)
                }

                fechaVisible = true
                btnFechaHora.setText("Eliminar fecha y hora")
                lyFechaHora.setVisibility(View.VISIBLE)

                cal.setTime(tarea.fecha)

            }

            if(tarea.prioridad == 2){
                swPrioritario.setChecked(true)
            }
            id=tarea.id

        } else{                                                 //NUEVA
            supportActionBar!!.title = "Nueva Tarea"
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

        btnFechaHora.setOnClickListener {
            addFechaHora()
        }
    }

    private fun addFechaHora() {
        var view: Int
        if (fechaVisible) {
            fechaVisible = false
            view = View.INVISIBLE
            btnFechaHora.setText("Añadir fecha y hora")
        } else {
            fechaVisible = true
            view = View.VISIBLE
            btnFechaHora.setText("Eliminar fecha y hora")
        }
        lyFechaHora.setVisibility(view)
        cal = Calendar.getInstance()
        tvFecha.text = getString(R.string.escoge_fecha)
        tvHora.text = getString(R.string.escoge_hora)
    }


    private fun anadirModificar() {
        tarea.titulo = etTitulo.text.toString()
        tarea.descripcion = etDescripcion.text.toString()
        var fecha: Date? = null
        if(!tvFecha.text.equals(getString(R.string.escoge_fecha)) || !tvHora.text.equals(getString(
                R.string.escoge_hora
            ))){
            fecha = cal.getTime()
        }

        tarea.fecha = fecha

        if (swPrioritario.isChecked){
            tarea.prioridad=2
        }

        val dbPersistencia = DbPersistenciaTareas(this)

        var res:Int
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
            cal.set(Calendar.SECOND, 1)
            tvHora.text = formatoHora.format(cal.time)
        }

        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    private fun ivCalendarioOnClick() {
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDay ->
            cal.set(mYear, mMonth, mDay, 0, 0, 0)
            tvFecha.text = formatoFecha.format(cal.time)
        },year,month,day)

        datePickerDialog.show()
    }

}
