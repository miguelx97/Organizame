package com.miguelmartin.organizame.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.miguelmartin.organizame.R
import com.miguelmartin.organizame.util.*
import com.miguelmartin.organizame.bbdd.DB_TABLE_TAREAS
import com.miguelmartin.organizame.bbdd.DbPersistenciaCategorias
import com.miguelmartin.organizame.bbdd.DbPersistenciaTareas
import com.miguelmartin.organizame.model.Categoria
import com.miguelmartin.organizame.model.Tarea
import kotlinx.android.synthetic.main.activity_add_tarea.*
import java.util.*
import android.view.KeyEvent.KEYCODE_BACK
import android.view.KeyEvent


const val IMPORTANTE = 1
const val NO_IMPORTANTE = 3

const val ANADIR = 1
const val MODIFICAR = 2


class AddTareaActivity : AppCompatActivity() {

    var id:Int? = null
    var cal = Calendar.getInstance()
    var tarea = Tarea()
    var fechaVisible = false
    var importante = false
    var estado = 0
    var idCategoria = 0

    lateinit var dbPersistenciaCategorias:DbPersistenciaCategorias
    lateinit var itemEliminar:MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tarea)
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.bringToFront();

        if (intent.getSerializableExtra(DB_TABLE_TAREAS) == null) estado = ANADIR
        else estado = MODIFICAR

        if (estado == MODIFICAR){     //MODIFICAR
            tarea = intent.getSerializableExtra(DB_TABLE_TAREAS) as Tarea
            supportActionBar!!.title = "Modificar nota"
//            btnAnadir.text = "Modificar"

            etTitulo.setText(tarea.titulo)
            etTitulo.visibility = View.INVISIBLE
            tvTitulo.setText(tarea.titulo)
            idCategoria = tarea.categoria.id

            etDescripcion.setText(tarea.descripcion)
            etDescripcion.visibility = View.INVISIBLE
            tvDescripcion.setText(tarea.descripcion)
            if (tarea.descripcion!!.isEmpty()){
                tvDescripcion.setText("Añadir descripción")
            }

/*
            if(tarea.fecha != null) {
                btnFecha.text = formatoFecha.format(tarea.fecha)
                cambiarEstadoItem(ivCalendario, true)
                if(formatoSegundos.format((tarea.fecha)!!).toInt() != 0){
                    btnHora.text = formatoHora.format(tarea.fecha)
                    cambiarEstadoItem(ivReloj, true)
                } else{
                    cambiarEstadoItem(ivReloj, false)
                    btnHora.text = getString(R.string.escoge_hora)
                }

                fechaVisible = true
                btnFechaHora.setText("Eliminar fecha y hora")
                lyFechaHora.setVisibility(View.VISIBLE)

                cal.setTime(tarea.fecha)
            } else{
                cambiarEstadoItem(ivCalendario, false)
                cambiarEstadoItem(ivReloj, false)
            }
*/
            if(tarea.prioridad == IMPORTANTE){
                importante = true
                cambiarEstadoItem(btnImportante, true)
            } else{
                importante = false
                cambiarEstadoItem(btnImportante, false)
            }

            if(tarea.categoria.id != 0){
                cambiarEstadoItem(btnCategorias, true)
                btnCategorias.text = tarea.categoria.titulo
            } else{
                cambiarEstadoItem(btnCategorias, false)
            }


//            btnEliminar.visibility = View.VISIBLE
            id=tarea.id

        } else{                                                 //NUEVA
            supportActionBar!!.title = "Nueva Nota"
//            btnEliminar.visibility = View.INVISIBLE

//            cambiarEstadoItem(ivCalendario, false)
//            cambiarEstadoItem(ivReloj, false)
            cambiarEstadoItem(btnImportante, false)
            cambiarEstadoItem(btnCategorias, false)
        }


//        btnAnadir.setOnClickListener { ocAnadirModificar() }

        btnFecha.setOnClickListener(){ ocFecha() }

        btnHora.setOnClickListener(){ ocHora() }

//        btnFechaHora.setOnClickListener { addFechaHora() }

        btnCategorias.setOnClickListener { modalCategorias() }

        btnImportante.setOnClickListener { ocImportante() }

//        btnEliminar.setOnClickListener{ ocEliminar() }

        tvTitulo.setOnClickListener {
            tvTitulo.visibility = View.INVISIBLE
            etTitulo.visibility = View.VISIBLE
        }
        tvDescripcion.setOnClickListener {
            tvDescripcion.visibility = View.INVISIBLE
            etDescripcion.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_gestion, menu)
        itemEliminar = menu!!.findItem(R.id.itemEliminar)
        if(estado == ANADIR){
            itemEliminar.setVisible(false)
        } else{
            itemEliminar.setVisible(true)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when(item.itemId){
                R.id.itemEliminar -> { //eliminar
                    ocEliminar()
                }
                R.id.itemGuardar -> { //guardar
                    ocAnadirModificar()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun ocEliminar() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.eliminar_nota))
        builder.setMessage(getString(R.string.seguro_eliminar_nota))
        builder.setPositiveButton(getString(R.string.si)){_, _ ->
            val dbPersistencia = DbPersistenciaTareas(this)
            val res = dbPersistencia.eliminar(tarea)
            if (res > 0){
                SetReminder(this).setTime()
                Toast.makeText(this, getString(R.string.nota_accion, "eliminada"), Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
            finish()
        }
        builder.setNegativeButton(getString(R.string.no)){dialog,_ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
/*
    private fun addFechaHora() {
        var view: Int
        if (fechaVisible) {
            fechaVisible = false
            view = View.INVISIBLE
            btnFechaHora.setText(getString(R.string.poner_recordatorio))
            cambiarEstadoItem(ivCalendario, false)
            cambiarEstadoItem(ivReloj, false)

            btnFecha.text = getString(R.string.escoge_fecha)
            btnHora.text = getString(R.string.escoge_hora)

        } else {
            fechaVisible = true
            view = View.VISIBLE
            btnFechaHora.setText(getString(R.string.eliminar_recordatorio))
        }
        lyFechaHora.visibility = view
        cal = Calendar.getInstance()
    }
*/

    private fun ocAnadirModificar() {
        tarea = setData()

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
            Toast.makeText(this, "la nota ha sido $action", Toast.LENGTH_LONG).show()
            if(tarea.fecha != null) SetReminder(this).setTime()
            finish()

        } else {
            Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
        }
    }

    private fun setData():Tarea {
        var tarea = Tarea(this.tarea)
        tarea.titulo = etTitulo.text.toString()
        tarea.descripcion = etDescripcion.text.toString()
        tarea.categoria.id = idCategoria
        var fecha: Date? = null
        if (!btnFecha.text.equals(getString(R.string.escoge_fecha)) || !btnHora.text.equals(getString(R.string.escoge_hora))) {
            fecha = cal.getTime()
        }

        tarea.fecha = fecha


        if (importante) {
            tarea.prioridad = IMPORTANTE
        } else {
            tarea.prioridad = NO_IMPORTANTE
        }

        return tarea
    }

    private fun ocHora() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener{_, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            cal.set(Calendar.SECOND, 1)
            btnHora.text = formatoHora.format(cal.time)
//            cambiarEstadoItem(ivReloj, true)
        }

        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    private fun ocFecha() {
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{_, mYear, mMonth, mDay ->
            cal.set(mYear, mMonth, mDay, 0, 0, 0)
            btnFecha.text = formatoFecha.format(cal.time)
//            cambiarEstadoItem(ivCalendario, true)
        },year,month,day)

        datePickerDialog.show()
    }

    fun modalCategorias(){
        val builder = AlertDialog.Builder(this)

        dbPersistenciaCategorias = DbPersistenciaCategorias(this)
        val listaCategorias: List<Categoria> = dbPersistenciaCategorias.getItems("%")
        val arrItems = arrayOfNulls<String>(listaCategorias.size + 1)
        var selectedItem = -1

        var i = 0
        arrItems[i++] = "Sin Categoría"
        listaCategorias.forEach {
            if(it.id == idCategoria){
                selectedItem = i
            }
            arrItems[i++] = it.titulo
        }

        builder.setTitle(getString(R.string.categorias))

        builder.setSingleChoiceItems(arrItems, selectedItem) {dialog, which ->
            idCategoria = 0
            listaCategorias.forEach {
                if(it.titulo.equals(arrItems[which])){
                    idCategoria = it.id
                }
            }

            if(idCategoria != 0){
                btnCategorias.setText(arrItems[which])
                cambiarEstadoItem(btnCategorias, true);

            } else{
                btnCategorias.setText(getString(R.string.categorias))
                cambiarEstadoItem(btnCategorias, false);
            }


            dialog.dismiss()

        }

        builder.setNeutralButton("Cancelar") { dialog,_ ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()

    }

    fun cambiarEstadoItem(boton:Button, activado:Boolean){
        var color = getColor(activado)

        val compoundDrawables = boton.getCompoundDrawables()
        val drawableLeft= compoundDrawables[0].mutate();
        drawableLeft.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN))

    }

    fun cambiarEstadoItem(imageView:ImageView, activado:Boolean){
        var color = getColor(activado)

        val mode = PorterDuff.Mode.SRC_ATOP
        imageView.setColorFilter(color, mode)
    }

    private fun getColor(activado: Boolean): Int {
        var color:Int
        if (activado) {
            color = ContextCompat.getColor(this, R.color.colorPrimary)
        } else {
            color = ContextCompat.getColor(this, android.R.color.darker_gray)
        }
        return color
    }


    private fun ocImportante() {
        if (!importante) {
            importante = true
            cambiarEstadoItem(btnImportante, true);
        } else {
            importante = false
            cambiarEstadoItem(btnImportante, false);
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        if(volver()){
            finish()
        }
        return false
    }

    override fun onBackPressed() {
        if(volver()){
            finish()
        }
    }

    private fun volver():Boolean {
        var tarea = setData()

        if (tarea.toString() == this.tarea.toString()){
            return true
        }

        alertVolver()

        return false
    }

    private fun alertVolver() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.volver))
        builder.setMessage(getString(R.string.seguro_descartar_cambios))
        builder.setPositiveButton(getString(R.string.descartar)){_, _ ->
            finish()
        }
        builder.setNeutralButton(getString(R.string.cancelar)){dialog,_ ->
            dialog.cancel()
        }
        builder.setNegativeButton(getString(R.string.guardar)){dialog,_ ->
            ocAnadirModificar()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}
