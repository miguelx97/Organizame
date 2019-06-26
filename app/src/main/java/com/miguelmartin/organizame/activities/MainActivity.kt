package com.miguelmartin.organizame.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
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
import android.widget.Toast
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var listaTareasCompleta: ArrayList<Tarea>
        lateinit var listaTareas: ArrayList<Tarea>
        lateinit var context: Context
        lateinit var adapter:AppAdapter
    }

    lateinit var dbPersistencia:DbPersistenciaTareas
    lateinit var dbPersistenciaCategorias:DbPersistenciaCategorias
    lateinit var rvTareas:RecyclerView
    private lateinit var iconoSwipe: Drawable
    private lateinit var fondoSwipe: ColorDrawable
    var selectedItems:BooleanArray? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Toolbar
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.title = getString(R.string.app_name)

        context = this

        //Float button
        fbAdd.setOnClickListener {
            //ir a AddTareaActivity
            var intent = Intent(this, AddTareaActivity::class.java)
            startActivity(intent)
        }
        fbCateg.setOnClickListener {
            //ir a AddTareaActivity
            var intent = Intent(this, GestionCategoriasActivity::class.java)
            startActivity(intent)
        }

        iconoSwipe = ContextCompat.getDrawable(context, R.drawable.archivar)!!
        fondoSwipe =   ColorDrawable(ContextCompat.getColor(context, R.color.colorArchivar))

    }

    override fun onResume() {
        super.onResume()
        cargarItems("", true)
        cargarCategorias()
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when(item.itemId){
//                R.id.itemNuevaCategoria -> { //ir a addCategorias
//                    var intent = Intent(this, GestionCategoriasActivity::class.java)
//                    startActivity(intent)
//                }
//
//                R.id.itemCategorias -> { //ir a Categorias
//                    modalCategorias()
//                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun cargarItems(categoria: String, verTodo:Boolean) {
        dbPersistencia = DbPersistenciaTareas(context)
        listaTareasCompleta = dbPersistencia.getTaresByCategoria(categoria, verTodo)
        listaTareas = ArrayList()
        listaTareasCompleta.forEach {
            listaTareas.add(it)
        }
        rellenarRecyclerView()
    }

    fun rellenarRecyclerView(){
        rvTareas = (context as Activity).findViewById(R.id.rvTareas)
        rvTareas.layoutManager = LinearLayoutManager(context)  //www.youtube.com/watch?v=NQWVpm5vdA8
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvTareas)
        adapter = AppAdapter(listaTareas)
        rvTareas.adapter = adapter
    }

    private fun cargarCategorias() {
        dbPersistenciaCategorias = DbPersistenciaCategorias(this)
        val categorias = dbPersistenciaCategorias.getItems("%")
        rellenarRecyclerViewCategorias(categorias)
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

    fun modalCategorias(){

        dbPersistenciaCategorias = DbPersistenciaCategorias(this)

        val listaCategorias: List<Categoria> = dbPersistenciaCategorias.getItems("%")

        val arrItems = arrayOfNulls<String>(listaCategorias.size)
        if (selectedItems == null)
                selectedItems =  BooleanArray(listaCategorias.size){false}

        var i = 0
        listaCategorias.forEach {
            arrItems[i++] = it.titulo
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.categorias))

        builder.setMultiChoiceItems(arrItems, selectedItems) { _, _, _ ->  }//onClick al checkear      dialog, which, isChecked

        builder.setNegativeButton("Cancelar"){ dialog, _ -> dialog.dismiss() }

        builder.setPositiveButton("Aceptar") {_, _ ->
            var lCategorias = ArrayList<String>()
            for (i in 0 until listaCategorias.size){
                if (selectedItems!![i]) {
                    lCategorias.add(listaCategorias[i].id.toString())
                }
            }

            val arrCategorias = Array<String>(lCategorias.size){""}
            lCategorias.toArray(arrCategorias)

            cargarItems("",false)

        }

        val dialog = builder.create()
        dialog.show()
    }

    //https://www.youtube.com/watch?v=eEonjkmox-0&list=WL&index=23&t=1s
    var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if(direction == ItemTouchHelper.RIGHT){
                    eliminar(listaTareas.get(viewHolder.adapterPosition))
                }


                else if(direction == ItemTouchHelper.LEFT)
                    archivar(listaTareas.get(viewHolder.adapterPosition))

            }

            private fun eliminar(tarea: Tarea){
                val listaTareasPosicion:Int = listaTareas.indexOf(tarea)
                val listaTareasCompletaPosicion:Int = listaTareasCompleta.indexOf(tarea)
                listaTareasCompleta.remove(tarea)
                listaTareas.remove(tarea)
                adapter.notifyItemRemoved(listaTareasPosicion)
                val res = dbPersistencia.eliminar(tarea)
                if (res > 0)
                    Snackbar.make(rvTareas, getString(R.string.nota_eliminada), Snackbar.LENGTH_LONG).setAction("Desacer") {
                        listaTareasCompleta.add(listaTareasCompletaPosicion, tarea)
                        listaTareas.add(listaTareasPosicion, tarea)
                        dbPersistencia.estadoInicial(tarea)
                        adapter.notifyItemInserted(listaTareasPosicion)

                    }.show()
                else
                    Toast.makeText(this@MainActivity, "ERROR", Toast.LENGTH_SHORT).show()
            }

            private fun archivar(tarea: Tarea){
                (listaTareasCompleta as ArrayList<Tarea>).remove(tarea)
                listaTareas.remove(tarea)
                adapter.notifyDataSetChanged()
                val res = dbPersistencia.archivar(tarea)
                if (res > 0)
                    Toast.makeText(this@MainActivity, getString(R.string.nota_eliminada), Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@MainActivity, "ERROR", Toast.LENGTH_SHORT).show()

                Toast.makeText(this@MainActivity, "Nota archivada", Toast.LENGTH_SHORT).show()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val iconMarginVertical = (viewHolder.itemView.height - iconoSwipe.intrinsicHeight) / 2

                if (dX > 0) {
                    fondoSwipe = ColorDrawable(ContextCompat.getColor(context, R.color.colorBorrar))
                    iconoSwipe = ContextCompat.getDrawable(context, R.drawable.papelera_blanca)!!
                    fondoSwipe.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                    iconoSwipe.setBounds(itemView.left + iconMarginVertical, itemView.top + iconMarginVertical,
                        itemView.left + iconMarginVertical + iconoSwipe.intrinsicWidth, itemView.bottom - iconMarginVertical)
                } else {
                    iconoSwipe = ContextCompat.getDrawable(context, R.drawable.archivar)!!
                    fondoSwipe =   ColorDrawable(ContextCompat.getColor(context, R.color.colorArchivar))
                    fondoSwipe.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    iconoSwipe.setBounds(itemView.right - iconMarginVertical - iconoSwipe.intrinsicWidth, itemView.top + iconMarginVertical,
                        itemView.right - iconMarginVertical, itemView.bottom - iconMarginVertical)
                    iconoSwipe.level = 0
                }

                fondoSwipe.draw(c)

                c.save()

                if (dX > 0)
                    c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                else
                    c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)

                iconoSwipe.draw(c)

                c.restore()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }


}