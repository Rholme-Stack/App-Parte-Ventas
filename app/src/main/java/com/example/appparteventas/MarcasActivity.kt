package com.example.appparteventas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.Toast

class MarcasActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marcas)

        //Quita la toolbar
        getSupportActionBar()?.hide()

        //setup
        setup()


// Create an ArrayAdapter using the string array and a default spinner layout
        val spinner: Spinner = findViewById(R.id.marcaSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.marcas,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


        //spiner forma de pago
        val spinner2: Spinner = findViewById(R.id.formaDePagospinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.formaDePago,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner2.adapter = adapter
        }



        //number picker numerico de 1 a 30
                val numberPicker: NumberPicker = findViewById (R.id.dtoNumberPicker)
                numberPicker.minValue =0
                numberPicker.maxValue =30
                numberPicker.wrapSelectorWheel = true
                numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->

                }






    }

    private fun setup() {
        title="Marcas"
    }
}