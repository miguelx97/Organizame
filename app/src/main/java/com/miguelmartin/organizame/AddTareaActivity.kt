package com.miguelmartin.organizame

import android.content.ContentValues
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.miguelmartin.organizame.bbdd.AppDatabaseCon
import com.miguelmartin.organizame.bbdd.DbWorkerThread
import com.miguelmartin.organizame.entity.Tarea
import kotlinx.android.synthetic.main.activity_add_tarea.*
import kotlinx.android.synthetic.main.activity_add_tarea.toolbar
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class AddTareaActivity : AppCompatActivity() {

    private var mDb: AppDatabaseCon? = null
    private lateinit var mDbWorkerThread: DbWorkerThread

    var id:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tarea)
        setSupportActionBar(toolbar as Toolbar?)

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        mDb = AppDatabaseCon.getInstance(this)

        id=intent.getStringExtra("")


        if (id!=null){
            supportActionBar!!.title = "Nueva Tarea"
            etTitulo.setText(intent.getStringExtra(""))
            etDescripcion.setText(intent.getStringExtra(""))
        } else{
            supportActionBar!!.title = "Modificar Tarea"
        }

            btnAddNotes.setOnClickListener {
            addNote()
        }
    }

    private fun addNote() {

        val titulo = etTitulo.text.toString();
        val descripcion = etDescripcion.text.toString();
        val fecha = Date()
        val tarea:Tarea = Tarea(0,titulo,descripcion,1,fecha);



        var values = ContentValues()
        values.put("", etTitulo.text.toString())
        values.put("", etDescripcion.text.toString())

        var res:Int = 0
        var action:String

        if (id==null){
            val task = Runnable { mDb?.appDao()?.insertar(tarea) }
            mDbWorkerThread.postTask(task)
            action = "added"
        } else{
            var selectionArgs= arrayOf(id.toString())
            val task = Runnable { mDb?.appDao()?.modificar(tarea) }
            mDbWorkerThread.postTask(task)
            action = "updated"
        }

        if (res > 0) {
            Toast.makeText(this, "note is $action", Toast.LENGTH_LONG).show()

            finish()
        } else {
            Toast.makeText(this, "note cannot be $action", Toast.LENGTH_LONG).show()
        }
    }

}
