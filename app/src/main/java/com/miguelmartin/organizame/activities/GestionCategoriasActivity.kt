package com.miguelmartin.organizame.activities

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.miguelmartin.organizame.R
import com.miguelmartin.organizame.bbdd.COL_TITULO_CATE
import com.miguelmartin.organizame.bbdd.DbPersistenciaCategorias
import com.miguelmartin.organizame.data.AppAdapterCategorias
import com.miguelmartin.organizame.model.Categoria
import com.miguelmartin.organizame.util.CLASE
import com.miguelmartin.organizame.util.SHARE_PREFERENCES
import kotlinx.android.synthetic.main.activity_gestion_categorias.*
import petrov.kristiyan.colorpicker.ColorPicker


var categoria = Categoria()
var cateColor:Int = Color.WHITE

class GestionCategoriasActivity : AppCompatActivity() {
    lateinit var gradientDrawable: GradientDrawable
    lateinit var dbPersistencia:DbPersistenciaCategorias
    lateinit var categorias:ArrayList<Categoria>
    lateinit var clase:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_categorias)

        dbPersistencia = DbPersistenciaCategorias(this)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Categorías"

        clase = intent.getExtras().getString(CLASE).toString()

        lyColores.setOnClickListener { ocColorPicker() }

        btnAnadir.setOnClickListener {ocAnadir() }

        btnNuevo.setOnClickListener { ocNuevo() }
    }

    private fun ocAnadir(){
        if (etTitulo.text.toString().isNullOrEmpty()) {
            Toast.makeText(this, "Debes añadir un título", Toast.LENGTH_SHORT).show()
        } else if(yaExiste()) {
            Toast.makeText(this, "La categoría ya existe", Toast.LENGTH_SHORT).show()
        } else{
            categoria.titulo = etTitulo.text.toString()
            categoria.color = cateColor
            Log.w("ocAnadir", categoria.toString())

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
                if (AddTareaActivity::class.toString().equals(clase)){
                    finish()
                } else{
                    cargarItems("%")
                }
                limpiarCampos()

            } else {
                Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
            }
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

        val colors: ArrayList<String> = arrayListOf("#FFFFFF", "#FFEBEE", "#FCE4EC", "#F3E5F5", "#EDE7F6", "#E8EAF6", "#E3F2FD", "#E1F5FE", "#E0F7FA", "#E0F2F1", "#E8F5E9", "#F1F8E9", "#F9FBE7", "#FFFDE7", "#FFF8E1", "#FFF3E0", "#FBE9E7", "#EFEBE9", "#ECEFF1", "#f7f7f7")

        colorPicker
            .setColors(colors)
            .setColumns(4)
            .setRoundColorButton(true)
            .setDefaultColorButton(categoria.color!!)
            .setTitle("Escoge color")
            .setOnFastChooseColorListener(object : ColorPicker.OnFastChooseColorListener {

                override fun setOnFastChooseColorListener(position: Int, color: Int) {
                    gradientDrawable.setColor(color)
                    cateColor = color
                    colorPicker.dismissDialog()
                }

                override fun onCancel() {}

            })
            .disableDefaultButtons(true)
            .show()
    }


    private fun cargarItems(filtro:String) {
        categorias = dbPersistencia.getItems(filtro)
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
        var viewColor: View = context.findViewById(R.id.viewColor) as View
        var btnAnadir: Button = context.findViewById(R.id.btnAnadir) as Button
        gradientDrawable  = viewColor.getBackground().mutate() as GradientDrawable

        categoria = categoriaAdap
        etTitulo.setText(categoriaAdap.titulo)
        cateColor = categoriaAdap.color!!
        gradientDrawable.setColor(categoriaAdap.color!!)
        btnAnadir.setText("Modificar")
    }

    fun yaExiste():Boolean{
        categorias.forEach {
            if (it.titulo!!.equals(etTitulo.text.toString()) && it.id != categoria.id ) return true
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return false
    }
    override fun onBackPressed() {
        finish()
    }
}
