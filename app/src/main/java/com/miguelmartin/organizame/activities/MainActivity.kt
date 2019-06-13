package com.miguelmartin.organizame.activities

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import com.miguelmartin.organizame.R
import com.miguelmartin.organizame.bbdd.DbPersistenciaCategorias
import com.miguelmartin.organizame.bbdd.DbPersistenciaTareas
import com.miguelmartin.organizame.data.AppAdapter
import com.miguelmartin.organizame.model.Categoria
import com.miguelmartin.organizame.model.Tarea

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private var listaTareas: List<Tarea>? = null
    lateinit var dbPersistencia:DbPersistenciaTareas
    lateinit var dbPersistenciaCategorias:DbPersistenciaCategorias
    var selectedItems:BooleanArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Toolbar
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.title = "Tareas"

        //Float button
        fbAdd.setOnClickListener {
            //ir a AddTareaActivity
            var intent = Intent(this, AddTareaActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        cargarItems("%")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)

        val sv: SearchView = menu.findItem(R.id.mSearch).actionView as SearchView

        val sm= getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                cargarItems("%"+ query +"%")
                return false
            }

            override fun onQueryTextChange(filtro: String): Boolean {
                rellenarRecyclerCiew(filtrar(filtro))
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when(item.itemId){
                R.id.itemNuevaCategoria -> { //ir a addCategorias
                    var intent = Intent(this, GestionCategoriasActivity::class.java)
                    startActivity(intent)
                }

                R.id.itemCategorias -> { //ir a Categorias
                    alertCategorias()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun cargarItems(filtro:String) {
        dbPersistencia = DbPersistenciaTareas(this)
       val tareas = dbPersistencia.getTodo()
//         val tareas = dbPersistencia.getItems(filtro)
        listaTareas = tareas
        rellenarRecyclerCiew(tareas)
    }

    private fun rellenarRecyclerCiew(itemsList:List<Tarea>){
        recyclerView.layoutManager = LinearLayoutManager(this)  //www.youtube.com/watch?v=NQWVpm5vdA8
        val adapter = AppAdapter(itemsList)
        recyclerView.adapter = adapter
    }

        private fun filtrar(filtro:String):List<Tarea>{
        val lisEjObjetosFiltrado = ArrayList<Tarea>()
        for (tarea in listaTareas!!) {
            if (tarea.titulo?.contains(filtro)!!) {
                lisEjObjetosFiltrado.add(tarea)
            }
        }
        return lisEjObjetosFiltrado
    }


    fun alertCategorias(){

        dbPersistenciaCategorias = DbPersistenciaCategorias(this)

        val listaCategorias: List<Categoria> = dbPersistenciaCategorias.getItems("%")

        val arrItems = arrayOfNulls<String>(listaCategorias.size)
        if (selectedItems == null)
                selectedItems =  BooleanArray(listaCategorias.size){ i ->  false}

        var i = 0
        listaCategorias.forEach {
            arrItems[i++] = it.titulo
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.categorias))

        builder.setMultiChoiceItems(arrItems, selectedItems) { dialog, which, isChecked ->  }//onClick al checkear

//        builder.setCancelable(true)

        builder.setNegativeButton("Cancelar"){ dialog, which -> dialog.dismiss() }

        builder.setPositiveButton("Aceptar") {dialog, which ->
            for (i in 0 until arrItems.size) {
                val checked = selectedItems!![i]
                if (checked) {
                    Log.w("Categ", arrItems[i])
                }
            }
        }

        val dialog = builder.create()
        dialog.show()
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