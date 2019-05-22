package com.miguelmartin.organizame

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.miguelmartin.organizame.bbdd.DbPersistencia
import com.miguelmartin.organizame.entity.Tarea

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val mUiHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Toolbar
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.title = "Tareas"

        //LISTA DE TAREAS
        cargarItems()

        //Float button
        fbAdd.setOnClickListener { view ->
            //ir a AddTareaActivity
            var intent = Intent(this, AddTareaActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarItems()
    }

    private fun cargarItems() {
        var dbPersistencia = DbPersistencia(this)
        val tareas = dbPersistencia.getTodos()
        recyclerView.layoutManager = LinearLayoutManager(this)  //www.youtube.com/watch?v=NQWVpm5vdA8
        val adapter = AppAdapter(tareas)
        recyclerView.adapter = adapter
    }



    fun getLists(): ArrayList<Tarea> {
        var lists = ArrayList<Tarea>()
        lists.add(Tarea(1, "Item 1", "Descripcion 1", 1, Date()))
        lists.add(Tarea(1, "Item 2", "Descripcion 2", 1, Date()))
        lists.add(Tarea(1, "Item 3", "Descripcion 3", 1, Date()))
        lists.add(Tarea(1, "Item 4", "Descripcion 4", 1, Date()))
        return lists;
    }

}