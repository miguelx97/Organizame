package com.miguelmartin.organizame.activities

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.miguelmartin.organizame.R
import com.miguelmartin.organizame.bbdd.DbPersistenciaCategorias
import com.miguelmartin.organizame.data.AppAdapterCategorias
import com.miguelmartin.organizame.model.Categoria
import kotlinx.android.synthetic.main.activity_gestion_categorias.*
import petrov.kristiyan.colorpicker.ColorPicker
import kotlin.collections.ArrayList

class GestionCategoriasActivity : AppCompatActivity() {
    var categoria = Categoria()

    lateinit var dbPersistencia:DbPersistenciaCategorias

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_categorias)

        dbPersistencia = DbPersistenciaCategorias(this)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Categorías"

        lyColores.setOnClickListener {
            conClickColores()
        }

        btnAnadir.setOnClickListener {
            categoria.titulo = etTitulo.text.toString()

            var res:Int
            var action:String

            if (true){
                res = dbPersistencia.insertar(categoria)
                action = "añadida"
            } else{
                res = dbPersistencia.modificar(categoria)
                action = "modificada"
            }

            if (res > 0) {
                Toast.makeText(this, "la categoría ha sido $action", Toast.LENGTH_LONG).show()
                etTitulo.setText("")
                cargarItems("%")

            } else {
                Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
            }

        }

    }

    override fun onResume() {
        super.onResume()
        cargarItems("%")
    }

    private fun conClickColores() {
        val colorPicker = ColorPicker(this)
        var gradientDrawable: GradientDrawable = viewColor.getBackground().mutate() as GradientDrawable

        val colors: ArrayList<String> = arrayListOf("#FFFFFF","#FBFCFC", "#F2D7D5", "#FADBD8", "#FDEDEC", "#E8DAEF", "#D4E6F1",
            "#D6EAF8", "#D1F2EB", "#D0ECE7", "#D4EFDF", "#D5F5E3", "#FCF3CF", "#FDEBD0", "#FAE5D3", "#F6DDCC")

        colorPicker
            .setColors(colors)
            .setColumns(4)
            .setRoundColorButton(true)
            .setDefaultColorButton(Color.parseColor("#ffffff"))
            .setOnChooseColorListener(object : ColorPicker.OnChooseColorListener {
                override fun onCancel() {}

                override fun onChooseColor(position: Int, color: Int) {
                    gradientDrawable.setColor(color)
                    colorPicker.dismissDialog()
                }
            }).show()
    }


    private fun cargarItems(filtro:String) {
        val categorias = dbPersistencia.getItems(filtro)
//        val categorias = getLists()
        rellenarRecyclerCiew(categorias)
    }

    fun getLists(): ArrayList<Categoria> {
        var lists = ArrayList<Categoria>()
        lists.add(Categoria(1,"item 1", 1))
        lists.add(Categoria(2,"item 2", 2))
        lists.add(Categoria(3,"item 3", 3))
        return lists;
    }

    private fun rellenarRecyclerCiew(itemsList:List<Categoria>){
        rvCategorias.layoutManager = LinearLayoutManager(this)
        val adapter = AppAdapterCategorias(itemsList)
        rvCategorias.adapter = adapter
    }
}
