package com.miguelmartin.organizame.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.widget.SearchView
import com.miguelmartin.organizame.R
import com.miguelmartin.organizame.bbdd.DbPersistenciaCategorias
import com.miguelmartin.organizame.bbdd.DbPersistenciaTareas
import com.miguelmartin.organizame.data.AppAdapter
import com.miguelmartin.organizame.data.AppAdapterCategoriasMain
import com.miguelmartin.organizame.model.Categoria
import com.miguelmartin.organizame.model.Tarea

import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.View
import android.widget.Toast
import com.miguelmartin.organizame.util.*
import com.miguelmartin.organizame.data.SwipeActions
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var listaTareasCompleta: ArrayList<Tarea>
        lateinit var listaTareas: ArrayList<Tarea>
        lateinit var context: Context
        lateinit var adapter:AppAdapter
        var estado:Int = ESTADO_INICIAL
        var idCategoria:Int = SIN_CATEGORIA
    }

    lateinit var dbPersistencia:DbPersistenciaTareas
    lateinit var dbPersistenciaCategorias:DbPersistenciaCategorias
    lateinit var rvTareas:RecyclerView

    var selectedItems:BooleanArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Toolbar
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.title = getString(R.string.app_name)

        context = this

        bottomNavigationMenu.setOnNavigationItemSelectedListener(bottomNavigationMenuListener)

        //Float button
        fbAdd.setOnClickListener {
            //ir a AddTareaActivity
            var intent = Intent(this, AddTareaActivity::class.java)
            startActivity(intent)
        }
        fbCateg.setOnClickListener {
            //ir a AddTareaActivity
            var intent = Intent(this, GestionCategoriasActivity::class.java)
            intent.putExtra(CLASE, MainActivity::class.toString());
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarItems()
        cargarCategorias()
        SetReminder(context).setTime()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)

        val sv: SearchView = menu.findItem(R.id.mSearch).actionView as SearchView

        val sm= getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(filtro: String): Boolean {
                filtrar(filtro)
                rellenarRecyclerView()
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }


    private val bottomNavigationMenuListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.item_inbox -> {
                estado = ESTADO_INICIAL
                cargarItems()
            }
            R.id.item_archivados -> {
                estado = ESTADO_ARCHIVADO
                cargarItems()
            }
            R.id.item_eliminados -> {
                estado = ESTADO_ELIMINADO
                val res = dbPersistencia.eliminarDefinitivoHaceUnDia()

                if (res > 0)
                    Log.w("Borrado hasta ayer", "OK")
                else
                    Log.w("Borrado hasta ayer", "ERROR")

                cargarItems()
            }
        }
        return@OnNavigationItemSelectedListener true
    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        if (item != null) {
//            when(item.itemId){
//                R.id.itemArchivados -> { //ir a addCategorias
//                    if(estado == ESTADO_INICIAL){
//                        estado = ESTADO_ARCHIVADO
//                        cargarItems()
//                        item.setIcon(R.drawable.inbox);
//                        supportActionBar!!.title = getString(R.string.archivado)
//                    } else{
//                        estado = ESTADO_INICIAL
//                        cargarItems()
//                        item.setIcon(R.drawable.archivar);
//                        supportActionBar!!.title = getString(R.string.app_name)
//                    }
//                }
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    fun cargarItems() {
        dbPersistencia = DbPersistenciaTareas(context)
        listaTareasCompleta = dbPersistencia.getTaresByCategoria(idCategoria, estado)
        listaTareas = ArrayList()
        listaTareasCompleta.forEach {
            listaTareas.add(it)
        }
        rellenarRecyclerView()
    }

    fun rellenarRecyclerView(){
        rvTareas = (context as Activity).findViewById(R.id.rvTareas)
        rvTareas.layoutManager = LinearLayoutManager(context)  //www.youtube.com/watch?v=NQWVpm5vdA8
        ItemTouchHelper(SwipeActions(this, dbPersistencia)).attachToRecyclerView(rvTareas)
        adapter = AppAdapter(listaTareas)
        rvTareas.adapter = adapter
    }

    private fun cargarCategorias() {
        dbPersistenciaCategorias = DbPersistenciaCategorias(this)
        val categorias = dbPersistenciaCategorias.getItems("%")
        if(!categorias.isEmpty()){
            val limpiarCategorias = Categoria(SIN_CATEGORIA, "Todo", Color.WHITE)
            categorias.add(0,limpiarCategorias)
            rellenarRecyclerViewCategorias(categorias)
            rvCategorias.visibility = View.VISIBLE
        } else{
            rvCategorias.visibility = View.GONE
        }
    }

    fun setIdCategoria(id:Int){
       idCategoria = id
    }

    private fun rellenarRecyclerViewCategorias(itemsList:List<Categoria>){
        rvCategorias.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val adapter = AppAdapterCategoriasMain(itemsList)
        rvCategorias.adapter = adapter
    }

    private fun filtrar(filtro:String){
        listaTareas = ArrayList()
        for (tarea in listaTareasCompleta!!) {
            if (tarea.titulo!!.toUpperCase().contains(filtro.toUpperCase())) {
                listaTareas.add(tarea)
            }
        }
    }

    fun modalFiltros(){

        val arrItems:Array<String> = arrayOf("Principal", "Archivados", "Eliminados")
        selectedItems =  BooleanArray(arrItems.size){false}


        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.filtros))

        builder.setMultiChoiceItems(arrItems, selectedItems) { _, _, _ ->  }//onClick al checkear      dialog, which, isChecked

        builder.setNegativeButton("Cancelar"){ dialog, _ -> dialog.dismiss() }

        builder.setNeutralButton("Limpiar"){_,_ -> }

        builder.setPositiveButton("Aceptar") {dialog, which ->
            var itemsSeleccionados = ArrayList<Int>()
            for (i in 0 until selectedItems!!.size){
                if (selectedItems!![i]) {   //si el item está seleccionado
                    itemsSeleccionados.add(i+1) //guarda la posicion+1 que es el número que representa el estado
                }
            }

            cargarItems()

        }

        val dialog = builder.create()
        dialog.show()
    }

}