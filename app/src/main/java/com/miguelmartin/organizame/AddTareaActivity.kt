package com.miguelmartin.organizame

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.Toast
import com.miguelmartin.organizame.bbdd.DB_TABLE
import com.miguelmartin.organizame.bbdd.DbPersistencia
import com.miguelmartin.organizame.entity.Tarea
import kotlinx.android.synthetic.main.activity_add_tarea.*
import kotlinx.android.synthetic.main.activity_add_tarea.toolbar
import java.text.SimpleDateFormat
import java.util.*

class AddTareaActivity : AppCompatActivity() {


    var id:Int? = null
    var cal = Calendar.getInstance()

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


        ivReloj.setOnClickListener(){
            ivRelojOnClick()
        }

        ivCalendario.setOnClickListener(){
            ivCalendarioOnClick()
        }
    }



    private fun anadir() {
        Log.w("Cal", SimpleDateFormat("dd MMM").format(cal.time))
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



    private fun ivRelojOnClick() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            tvHora.text = SimpleDateFormat("HH:mm").format(cal.time)
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
            tvFecha.text = SimpleDateFormat("dd MMM").format(cal.time)
        },year,month,day)

        datePickerDialog.show()
    }


}
