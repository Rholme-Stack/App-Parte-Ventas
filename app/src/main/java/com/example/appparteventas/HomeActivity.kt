package com.example.appparteventas

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*

enum class ProviderType {
    BASIC

}

class HomeActivity : AppCompatActivity() {

    private val db= FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Quita la toolbar
        getSupportActionBar()?.hide()

        //Setup
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider= bundle?.getString("provider")
        setup( email?:"", provider?: "")

        //Guardado de datos

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()

        // Create an ArrayAdapter using the string array and a default spinner layout
        val spinnerTipoVisit: Spinner = findViewById(R.id.tipoDeVisitaSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.tipoDeVisita,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerTipoVisit.adapter = adapter
        }


    }

    private fun setup(email:String, provider: String) {
        title = "Inicio"
        emailTextView.text= email
        providertextView.text= provider




       //boton de logout
        finalizarButton.setOnClickListener {


            //borrado de datos
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()


            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        //boton de guardar en firestore
        saveMarcas_imageButton.setOnClickListener{
           if(cliente_ET.text.isEmpty() && comentariosMultiLine.text.isEmpty()) {
               val builder = AlertDialog.Builder(this)
               builder.setTitle("Error")
               builder.setMessage("Es necesário poner el cliente y comentarios generales!")
               builder.setPositiveButton("Aceptar", null)
               val dialog: AlertDialog = builder.create()
               dialog.show()

           }else{
               //crea el objeto
                   var t = Visitas()
                    t.cliente = cliente_ET.text.toString()
                    t.comentarios= comentariosMultiLine.text.toString()





               //se crea una variable con el hashmap


               val vendedor = hashMapOf("vendedor" to emailTextView.text.toString(),
                   "cliente" to t.cliente,
                   "comentarios" to t.comentarios,
                   "fecha" to FieldValue.serverTimestamp(),
                            )

               //añade a la base de datos
               db.collection("vendedores")
                   .add(vendedor)
                   .addOnSuccessListener { documentReference ->
                       Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")

                       //pantalla de aviso de guardado
                       val builder = AlertDialog.Builder(this)
                       builder.setTitle("Guardado")
                       builder.setMessage("Se ha guardado el registro")
                       builder.setPositiveButton("Aceptar", null)
                       val dialog: AlertDialog = builder.create()
                       dialog.show()
                   }
                   .addOnFailureListener { e ->
                       Log.w(TAG, "Error adding document", e)
                   }


               //funcion para limpiar cajas
               limpiarCajas()

           }





        }

        edit_imageButton.setOnClickListener{
            db.collection("vendedores").document(email).get().addOnSuccessListener {
                cliente_ET.setText(it.get("cliente")as String?)
                comentariosMultiLine.setText(it.get("comentarios")as String?)
            }
            //pantalla dfe aviso de guardado
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Guardado")
            builder.setMessage("Se ha modificado el registro")
            builder.setPositiveButton("Aceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        delete_imageButton.setOnClickListener{
            db.collection("Users").document(email).delete()

            //pantalla dfe aviso de eliminado
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Eliminado")
            builder.setMessage("Se ha eliminado el registro")
            builder.setPositiveButton("Aceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()

        }


        //boton añadir marca 1
        anadirMarcaButton.setOnClickListener{

            anadirMarcaButton.visibility = View.GONE
            marca1Container.isVisible= true

            //setup spiner y number picker marca 1


            val spinnerMarca1: Spinner = findViewById(R.id.marca1Spinner)
            ArrayAdapter.createFromResource(
                this,
                R.array.marcas,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinnerMarca1.adapter = adapter
            }


            val spinnerFormaPago1: Spinner = findViewById(R.id.formaDePago1Spinner)
            ArrayAdapter.createFromResource(
                this,
                R.array.formaDePago,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinnerFormaPago1.adapter = adapter
            }

            //numeber picker dto

            //number picker numerico de 1 a 30
            val n1: NumberPicker = findViewById (R.id.dto1NumberPicker)
            n1.minValue =0
            n1.maxValue =30
            n1.wrapSelectorWheel = true
            n1.setOnValueChangedListener { picker, oldVal, newVal ->

            }



        }

        //boton añadir marca 2
        anadirMarca1Button.setOnClickListener{

            anadirMarca1Button.visibility = View.GONE
            marca2Container.isVisible= true

            //setup spiner y number picker marca 2


            val spinnerMarca2: Spinner = findViewById(R.id.marca2Spinner)
            ArrayAdapter.createFromResource(
                this,
                R.array.marcas,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinnerMarca2.adapter = adapter
            }


            val spinnerFormaPago2: Spinner = findViewById(R.id.formaDePago2Spinner)
            ArrayAdapter.createFromResource(
                this,
                R.array.formaDePago,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinnerFormaPago2.adapter = adapter
            }

            //numeber picker dto

            //number picker numerico de 1 a 30
            val n2: NumberPicker = findViewById (R.id.dto2NumberPicker)
            n2.minValue =0
            n2.maxValue =30
            n2.wrapSelectorWheel = true
            n2.setOnValueChangedListener { picker, oldVal, newVal ->

            }
        }

        //boton añadir marca 3
        anadirMarca2Button2.setOnClickListener{
            anadirMarca2Button2.visibility = View.GONE
            marca3Container.isVisible= true

            //setup spiner y number picker marca 3


            val spinnerMarca3: Spinner = findViewById(R.id.marca3Spinner)
            ArrayAdapter.createFromResource(
                this,
                R.array.marcas,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinnerMarca3.adapter = adapter
            }


            val spinnerFormaPago3: Spinner = findViewById(R.id.formaDePago3Spinner)
            ArrayAdapter.createFromResource(
                this,
                R.array.formaDePago,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinnerFormaPago3.adapter = adapter
            }

            //numeber picker dto

            //number picker numerico de 1 a 30
            val n3: NumberPicker = findViewById (R.id.dto3NumberPicker)
            n3.minValue =0
            n3.maxValue =30
            n3.wrapSelectorWheel = true
            n3.setOnValueChangedListener { picker, oldVal, newVal ->

            }
        }



    }



    private fun limpiarCajas() {
        //funciona para limpiar cajas
        cliente_ET.setText("")
        comentariosMultiLine.setText("")
    }
}


