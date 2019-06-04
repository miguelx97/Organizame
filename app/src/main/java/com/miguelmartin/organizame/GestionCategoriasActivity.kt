package com.miguelmartin.organizame

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_gestion_categorias.*
import petrov.kristiyan.colorpicker.ColorPicker

class GestionCategoriasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_categorias)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Categorías"

        lyColores.setOnClickListener {

            val colors = ArrayList<String>()
            colors.add("#FBFCFC")
            colors.add("#F2D7D5")
            colors.add("#FADBD8")
            colors.add("#FDEDEC")
            colors.add("#E8DAEF")
            colors.add("#D4E6F1")
            colors.add("#D6EAF8")
            colors.add("#D1F2EB")
            colors.add("#D0ECE7")
            colors.add("#D4EFDF")
            colors.add("#D5F5E3")
            colors.add("#FCF3CF")
            colors.add("#FDEBD0")
            colors.add("#FAE5D3")
            colors.add("#F6DDCC")


            val colorPicker = ColorPicker(this)
            var gradientDrawable: GradientDrawable = viewColor.getBackground().mutate() as GradientDrawable

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
                }) .show()
        }
    }
}
