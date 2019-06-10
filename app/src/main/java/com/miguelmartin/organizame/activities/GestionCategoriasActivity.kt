package com.miguelmartin.organizame.activities

import android.app.Activity
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.miguelmartin.organizame.R
import com.miguelmartin.organizame.bbdd.DbPersistenciaCategorias
import com.miguelmartin.organizame.data.AppAdapterCategorias
import com.miguelmartin.organizame.model.Categoria
import kotlinx.android.synthetic.main.activity_gestion_categorias.*
import petrov.kristiyan.colorpicker.ColorPicker

var categoria = Categoria()

class GestionCategoriasActivity : AppCompatActivity() {
    lateinit var gradientDrawable: GradientDrawable
    lateinit var dbPersistencia:DbPersistenciaCategorias

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_categorias)

        dbPersistencia = DbPersistenciaCategorias(this)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Categorías"

        lyColores.setOnClickListener { ocColorPicker() }

        btnAnadir.setOnClickListener {ocAnadir() }

        btnNuevo.setOnClickListener { ocNuevo() }
    }

    private fun ocAnadir() {
        categoria.titulo = etTitulo.text.toString()
        Log.w("ocAnadir", categoria.toString())
        if (!categoria.titulo.isNullOrEmpty()) {
            var res: Int
            var action: String

            if (categoria.id > 0) {
                res = dbPersistencia.modificar(categoria)
                action = "modificada"
            } else {
                res = dbPersistencia.insertar(categoria)
                action = "añadida"
            }

            if (res > 0) {
                gradientDrawable = viewColor.getBackground().mutate() as GradientDrawable
                Toast.makeText(this, "la categoría ha sido $action", Toast.LENGTH_LONG).show()
                limpiarCampos()
                cargarItems("%")

            } else {
                Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Debes añadir un título", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos() {
        etTitulo.setText("")
        gradientDrawable = viewColor.getBackground().mutate() as GradientDrawable
        gradientDrawable.setColor(0)
        categoria = Categoria()
        btnAnadir.setText(getResources().getString(R.string.anadir))
    }


    private fun ocNuevo() {
        limpiarCampos()
    }

    override fun onResume() {
        super.onResume()
        cargarItems("%")
    }

    private fun ocColorPicker() {
        val colorPicker = ColorPicker(this)
        gradientDrawable  = viewColor.getBackground().mutate() as GradientDrawable

        val colors: ArrayList<String> = arrayListOf("#FFFFFF","#FBFCFC", "#F2D7D5", "#FADBD8", "#FDEDEC", "#E8DAEF", "#D4E6F1",
            "#D6EAF8", "#D1F2EB", "#D0ECE7", "#D4EFDF", "#D5F5E3", "#FCF3CF", "#FDEBD0", "#FAE5D3", "#F6DDCC")

        colorPicker
            .setColors(colors)
            .setColumns(4)
            .setRoundColorButton(true)
            .setDefaultColorButton(categoria.color!!)
            .setOnChooseColorListener(object : ColorPicker.OnChooseColorListener {
                override fun onCancel() {}

                override fun onChooseColor(position: Int, color: Int) {
                    if(!(position != 0 && color == 0)){
                        gradientDrawable.setColor(color)
                        categoria.color = color
                    }

                    colorPicker.dismissDialog()
                }
            }).show()
    }


    private fun cargarItems(filtro:String) {
        val categorias:ArrayList<Categoria> = dbPersistencia.getItems(filtro) as ArrayList<Categoria>
        rellenarRecyclerCiew(categorias)
    }

    fun getLists(): ArrayList<Categoria> {
        var lists = ArrayList<Categoria>()
        lists.add(Categoria(1,"item 1", 1))
        lists.add(Categoria(2,"item 2", 2))
        lists.add(Categoria(3,"item 3", 3))
        return lists;
    }

    private fun rellenarRecyclerCiew(itemsList:ArrayList<Categoria>){
        rvCategorias.layoutManager = LinearLayoutManager(this)
        val adapter = AppAdapterCategorias(itemsList, this)
        rvCategorias.adapter = adapter
    }

    fun setDatos(categoriaAdap: Categoria, context:Context){
        var etTitulo: EditText = (context as Activity).findViewById(R.id.etTitulo) as EditText
        var viewColor: View = (context as Activity).findViewById(R.id.viewColor) as View
        var btnAnadir: Button = (context as Activity).findViewById(R.id.btnAnadir) as Button
        gradientDrawable  = viewColor.getBackground().mutate() as GradientDrawable

        categoria = categoriaAdap
        etTitulo.setText(categoriaAdap.titulo)
        gradientDrawable.setColor(categoriaAdap.color!!)
        btnAnadir.setText("Modificar")
    }
}
