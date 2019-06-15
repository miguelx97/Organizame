package com.miguelmartin.organizame.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.miguelmartin.organizame.R
import com.miguelmartin.organizame.bbdd.DB_TABLE_TAREAS
import com.miguelmartin.organizame.bbdd.DbPersistenciaCategorias
import com.miguelmartin.organizame.bbdd.DbPersistenciaTareas
import com.miguelmartin.organizame.constantes.formatoFecha
import com.miguelmartin.organizame.constantes.formatoHora
import com.miguelmartin.organizame.model.Categoria
import com.miguelmartin.organizame.model.Tarea
import kotlinx.android.synthetic.main.activity_add_tarea.*
import java.util.*
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.content.res.AppCompatResources
import android.widget.Button

const val IMPORTANTE = 1
const val NO_IMPORTANTE = 3

class AddTareaActivity : AppCompatActivity() {

    var id:Int? = null
    var cal = Calendar.getInstance()
    var tarea = Tarea()
    var fechaVisible = false
    var importante = false
    lateinit var dbPersistenciaCategorias:DbPersistenciaCategorias

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

            if(tarea.prioridad == IMPORTANTE){
                cambiarEstadoItem(R.drawable.estrella, true, btnImportante);
            } else{
                cambiarEstadoItem(R.drawable.estrella, false, btnImportante);
            }

            if(tarea.categoria.id != 0){
                cambiarEstadoItem(R.drawable.carpeta_add_tarea, true, btnCategorias);
            } else{
                cambiarEstadoItem(R.drawable.carpeta_add_tarea, false, btnCategorias);
            }

            id=tarea.id

        } else{                                                 //NUEVA
            supportActionBar!!.title = "Nueva Tarea"
        }

        btnAnadir.setOnClickListener { anadirModificar() }

        lyFecha.setOnClickListener(){ ivCalendarioOnClick() }

        lyHora.setOnClickListener(){ ivRelojOnClick() }

        btnFechaHora.setOnClickListener { addFechaHora() }

        btnCategorias.setOnClickListener { modalCategorias() }

        btnImportante.setOnClickListener { ocImportante() }
    }

    private fun addFechaHora() {
        var view: Int
        if (fechaVisible) {
            fechaVisible = false
            view = View.INVISIBLE
            btnFechaHora.setText(getString(R.string.recordatorio))
        } else {
            fechaVisible = true
            view = View.VISIBLE
            btnFechaHora.setText(getString(R.string.eliminar_recordatorio))
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

        if (importante){
            tarea.prioridad = IMPORTANTE
        } else{
            tarea.prioridad = NO_IMPORTANTE
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

    fun modalCategorias(){
        val builder = AlertDialog.Builder(this)

        dbPersistenciaCategorias = DbPersistenciaCategorias(this)
        val listaCategorias: List<Categoria> = dbPersistenciaCategorias.getItems("%")
        val arrItems = arrayOfNulls<String>(listaCategorias.size + 1)

        var i = 0
        arrItems[i++] = "Sin Categoría"
        listaCategorias.forEach {
            arrItems[i++] = it.titulo
        }

        builder.setTitle(getString(R.string.categorias))

        builder.setSingleChoiceItems(arrItems, -1) {dialog, which ->
            tarea.categoria!!.id = 0
            listaCategorias.forEach {
                if(it.titulo.equals(arrItems[which])){
                    tarea.categoria!!.id = it.id
                }
            }

            if(tarea.categoria.id != 0){
                btnCategorias.setText(arrItems[which])
                cambiarEstadoItem(R.drawable.carpeta_add_tarea, true, btnCategorias);

            } else{
                btnCategorias.setText(getString(R.string.categorias))
                cambiarEstadoItem(R.drawable.carpeta_add_tarea, false, btnCategorias);
            }


            dialog.dismiss()

        }

        builder.setNeutralButton("Cancelar") { dialog, which ->
            // Do something when click the neutral button
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()

    }

    fun cambiarEstadoItem(@DrawableRes drawable:Int, activado:Boolean, boton:Button){
        var color = 0
        if(activado){
            color = ContextCompat.getColor(this, R.color.colorPrimary)
        } else{
            color = ContextCompat.getColor(this, android.R.color.darker_gray)
        }
        
        val unwrappedDrawable = AppCompatResources.getDrawable(this, drawable)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, color)
        boton.setCompoundDrawablesWithIntrinsicBounds( drawable, 0, 0, 0);
    }


    private fun ocImportante() {
        if (!importante) {
            importante = true
            cambiarEstadoItem(R.drawable.estrella, true, btnImportante);
        } else {
            importante = false
            cambiarEstadoItem(R.drawable.estrella, false, btnImportante);
        }
    }

}
