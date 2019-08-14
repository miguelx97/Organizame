package com.miguelmartin.organizame.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.Button
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
import java.util.concurrent.TimeUnit


const val IMPORTANTE = 1
const val NO_IMPORTANTE = 3

class AddTareaActivity : AppCompatActivity() {

    var cal = Calendar.getInstance()
    var tarea = Tarea()
    var importante = false
    var nuevo:Boolean = false
    var idCategoria = 0
    data class Recordatorio(var dias: Long = 0, var horas: Long = 0, var minutos: Long = 0)
    lateinit var dbPersistenciaCategorias:DbPersistenciaCategorias
    lateinit var itemEliminar:MenuItem
    lateinit var listaCategorias: List<Categoria>
    lateinit var listaCategoriasOncreate: MutableList<Categoria>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tarea)
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.bringToFront();

        dbPersistenciaCategorias = DbPersistenciaCategorias(this)

        listaCategoriasOncreate = dbPersistenciaCategorias.getItems("%")

        if (intent.getSerializableExtra(DB_TABLE_TAREAS) == null) nuevo = true

        if (!nuevo){     //MODIFICAR
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            tarea = intent.getSerializableExtra(DB_TABLE_TAREAS) as Tarea
            supportActionBar!!.title = "Modificar nota"

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

            if(tarea.fecha != null) {
                btnFecha.text = formatoFecha.format(tarea.fecha)
                cambiarEstadoItem(btnFecha, true)
                if(formatoSegundos.format((tarea.fecha)!!).toInt() != 0){
                    btnHora.text = formatoHora.format(tarea.fecha)
                    cambiarEstadoItem(btnHora, true)
                } else{
                    cambiarEstadoItem(btnHora, false)
                    btnHora.text = getString(R.string.escoge_hora)
                }

                cal.setTime(tarea.fecha)
            } else{
                cambiarEstadoItem(btnFecha, false)
                cambiarEstadoItem(btnHora, false)

                estadoRecordatorio(false)
            }

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


            if(tarea.fechaNotificacion != null){
                val recordatorio:Recordatorio = recordatorioToObject(tarea.fechaNotificacion!!, tarea.fecha!!)
                etRecDias.setText(""+recordatorio.dias)
                etRecHoras.setText(""+recordatorio.horas)
                etRecMinutos.setText(""+recordatorio.minutos)
            }

        } else{                                                 //NUEVA
            supportActionBar!!.title = "Nueva Nota"

            cambiarEstadoItem(btnImportante, false)
            cambiarEstadoItem(btnCategorias, false)

            cambiarEstadoItem(btnFecha, false)
            cambiarEstadoItem(btnHora, false)

            estadoRecordatorio(false)
        }

        btnFecha.setOnClickListener(){ ocFecha() }
        btnHora.setOnClickListener(){ ocHora() }
        btnCategorias.setOnClickListener { modalCategorias() }
        btnImportante.setOnClickListener { ocImportante() }
        btnLimpiarTiempo.setOnClickListener { ocLimpiarTiempo() }
        btnLimpiarRecordatorio.setOnClickListener { ocLimpiarRecordatorio() }

        tvTitulo.setOnClickListener {
            tvTitulo.visibility = View.INVISIBLE
            etTitulo.visibility = View.VISIBLE
        }
        tvDescripcion.setOnClickListener {
            tvDescripcion.visibility = View.INVISIBLE
            etDescripcion.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()

        listaCategorias = dbPersistenciaCategorias.getItems("%")

        if(listaCategorias.isNotEmpty()){
            val presuntaNuevaCategoria = listaCategorias.last()

            if (presuntaNuevaCategoria.id != listaCategoriasOncreate.last().id){
                idCategoria = presuntaNuevaCategoria.id
                categoria.titulo = presuntaNuevaCategoria.titulo
                listaCategoriasOncreate = listaCategorias.toMutableList()
                btnCategorias.text = presuntaNuevaCategoria.titulo
                cambiarEstadoItem(btnCategorias, true);
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_gestion, menu)
        itemEliminar = menu!!.findItem(R.id.itemEliminar)
        if(nuevo){
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

    private fun ocAnadirModificar() {
        tarea = setData()

        val dbPersistencia = DbPersistenciaTareas(this)

        var res:Int
        var action:String

        if (nuevo){
            res = dbPersistencia.insertar(tarea)
            action = "añadida"
        } else{
            res = dbPersistencia.modificar(tarea)
            action = "modificada"
        }

        if (res > 0) {
            Toast.makeText(this, "la nota ha sido $action", Toast.LENGTH_LONG).show()
            if(tarea.fechaNotificacion != null) SetReminder(this).setTime()
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

        if(etRecMinutos.text.toString().isNotEmpty() || etRecHoras.text.toString().isNotEmpty() || etRecDias.text.toString().isNotEmpty()){
            var recordatorio = Recordatorio()

            if(etRecMinutos.text.toString().isNotEmpty()) recordatorio.minutos = etRecMinutos.text.toString().toLong()
            else recordatorio.minutos = 0

            if(etRecHoras.text.toString().isNotEmpty()) recordatorio.horas = etRecHoras.text.toString().toLong()
            else recordatorio.horas = 0

            if(etRecDias.text.toString().isNotEmpty()) recordatorio.dias = etRecDias.text.toString().toLong()
            else recordatorio.dias = 0

            tarea.fechaNotificacion = recordatorioToFecha(recordatorio, fecha!!)
        } else{
            tarea.fechaNotificacion = null
        }


        return tarea
    }

    private fun ocHora() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener{_, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            cal.set(Calendar.SECOND, 1)
            btnHora.text = formatoHora.format(cal.time)
            cambiarEstadoItem(btnHora, true)
            estadoRecordatorio(true)
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
            cambiarEstadoItem(btnFecha, true)
            estadoRecordatorio(true)
        },year,month,day)

        datePickerDialog.show()
    }

    private fun ocLimpiarTiempo(){
        cal = Calendar.getInstance()
        cambiarEstadoItem(btnFecha, false)
        cambiarEstadoItem(btnHora, false)

        btnFecha.text = getString(R.string.escoge_fecha)
        btnHora.text = getString(R.string.escoge_hora)

        ocLimpiarRecordatorio()
        estadoRecordatorio(false)
    }

    private fun ocLimpiarRecordatorio(){
        etRecDias.text = null
        etRecDias.clearFocus()
        etRecHoras.text = null
        etRecHoras.clearFocus()
        etRecMinutos.text = null
        etRecMinutos.clearFocus()

    }

    private fun estadoRecordatorio(estado:Boolean) {
        etRecDias.setEnabled(estado);
        etRecHoras.setEnabled(estado);
        etRecMinutos.setEnabled(estado);
    }

    fun modalCategorias(){
        val builder = AlertDialog.Builder(this)


        val arrItems = arrayOfNulls<String>(listaCategorias.size + 2)
        var selectedItem = -1

        var i = 0
        arrItems[i++] = "Sin Categoría"
        listaCategorias.forEach {
            if(it.id == idCategoria){
                selectedItem = i
            }
            arrItems[i++] = it.titulo
        }
        arrItems[i++] = "Nueva Categoría"

        builder.setTitle(getString(R.string.categorias))

        builder.setSingleChoiceItems(arrItems, selectedItem) {dialog, which ->
            idCategoria = 0
            listaCategorias.forEach {
                if(it.titulo.equals(arrItems[which])){
                    idCategoria = it.id
                }
            }

            //Sin categoria           Nueva Categoría
            if(which != 0 && which != listaCategorias.size + 1){
                btnCategorias.setText(arrItems[which])
                cambiarEstadoItem(btnCategorias, true);

            } else{
                btnCategorias.setText(getString(R.string.categorias))
                cambiarEstadoItem(btnCategorias, false);
            }

            dialog.dismiss()

            if(which == listaCategorias.size + 1){
                var intent = Intent(this, GestionCategoriasActivity::class.java)
                intent.putExtra(CLASE, AddTareaActivity::class.toString());
                startActivity(intent)
            }


        }

        builder.setNeutralButton("Cancelar") { dialog,_ ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()

    }



/*
    fun showDialog(sdTarea:Tarea) {
        val builder = AlertDialog.Builder(this)

        var etTitle: EditText? = null
        var etDes: EditText? = null

        val inflater = getLayoutInflater()
        val view = inflater.inflate(R.layout.dialog_add_categoria, null)

        etTitle = view.findViewById(R.id.etTitle)
        etDes = view.findViewById(R.id.etDes)

        if (sdTarea.noteId!=null){
            builder.setTitle("Editar Nota")
            etTitle!!.setText(sdTarea.noteName)
            etDes!!.setText(sdTarea.noteDes)
        } else{
            builder.setTitle("Nueva Nota")
        }

        builder.setView(view)

        builder.setPositiveButton("Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                addNote(sdTarea)
                loadItems("%")
                dialog.dismiss()
            })

        builder.setNegativeButton("Cancelar",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
            })

        val dialog = builder.create()
        dialog.show()
    }
    */


    fun cambiarEstadoItem(boton:Button, activado:Boolean){
        var color = getColor(activado)

        val compoundDrawables = boton.getCompoundDrawables()
        val drawableLeft= compoundDrawables[0].mutate();
        drawableLeft.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN))

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

    private fun recordatorioToObject(fechaNotificacion:Date, fecha:Date):Recordatorio{
        val milisec = fecha.getTime() - fechaNotificacion.getTime()
        var minutos = milisec/60000
        val dias = TimeUnit.MINUTES.toDays(minutos)
        minutos -= TimeUnit.DAYS.toMinutes(dias)
        val horas = TimeUnit.MINUTES.toHours(minutos)
        minutos -= TimeUnit.HOURS.toMinutes(horas)

        return Recordatorio(dias, horas,  minutos)
    }

    private fun recordatorioToFecha(recordatorio:Recordatorio, fecha: Date):Date{
        val milisec = (recordatorio.minutos
        + (recordatorio.horas*60)
        + (recordatorio.dias*24*60))*60000

        return Date(fecha.getTime() - milisec)
    }

}
