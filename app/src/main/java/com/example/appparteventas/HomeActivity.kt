package com.example.appparteventas

import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Insets.add
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.core.view.isVisible
import com.example.appparteventas.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*


enum class ProviderType {
    BASIC

}

class HomeActivity : AppCompatActivity() {


    private lateinit var binding: ActivityHomeBinding

    private val db= FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.emailTextView.text= email
        binding.providertextView.text= provider




       //boton de logout
        binding.finalizarButton.setOnClickListener {

          
            //borrado de datos
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()


            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        //boton de guardar en firestore
        binding.saveButton.setOnClickListener{
           if(binding.clienteET.text.isEmpty() && binding.comentariosMultiLine.text.isEmpty()) {
               val builder = AlertDialog.Builder(this)
               builder.setTitle("Error")
               builder.setMessage("Es necesário poner el cliente y comentarios generales!")
               builder.setPositiveButton("Aceptar", null)
               val dialog: AlertDialog = builder.create()
               dialog.show()

           }else{
               //crea el objeto
                   var t = Visitas()
                    t.cliente = binding.clienteET.text.toString()
                    t.tipoVisita = binding.tipoDeVisitaSpinner.selectedItem.toString()
                    t.comentarios= binding.comentariosMultiLine.text.toString()

                    var m1 = Marcas()
                    var m2 = Marcas()
                    var m3 = Marcas()
                    m1.marca1 = binding.marca1Spinner.selectedItem.toString()
                    m1.formaPago1 = binding.formaDePago1Spinner.selectedItem.toString()
                    m1.dto1 = binding.dto1NumberPicker.value.toString()
                    m1.comentarios1 = binding.comentarios1MultiLineMarcas.text.toString()

                        m2.marca2 = binding.marca2Spinner.selectedItem.toString()
                       m2.formaPago2 = binding.formaDePago2Spinner.selectedItem.toString()
                       m2.dto2 = binding.dto2NumberPicker.value.toString()
                       m2.comentarios2 = binding.comentarios2MultiLineMarcas.text.toString()

                           m3.marca3 = binding.marca3Spinner.selectedItem.toString()
                           m3.formaPago3 = binding.formaDePago3Spinner.selectedItem.toString()
                           m3.dto3 = binding.dto3NumberPicker.value.toString()
                           m3.comentarios3 = binding.comentarios3MultiLineMarcas.text.toString()







               //se crea una variable con el hashmap


               val vendedor = hashMapOf("vendedor" to binding.emailTextView.text.toString(),
                   "cliente" to t.cliente,
                   "Tipo de Visita" to t.tipoVisita,
                   "comentarios" to t.comentarios,
                   "fecha" to FieldValue.serverTimestamp(),
               )

                val marca1 = hashMapOf("Marca 1" to m1.marca1,
                    "Forma de pago" to m1.formaPago1,
                    "Descuento" to m1.dto1,
                    "Comentarios" to m1.comentarios1,
                )

               val marca2 = hashMapOf("Marca 2" to m2.marca2,
                   "Forma de pago" to m2.formaPago2,
                   "Descuento" to m2.dto2,
                   "Comentarios" to m2.comentarios2,
               )

               val marca3 = hashMapOf("Marca 3" to m3.marca3,
                   "Forma de pago" to m3.formaPago3,
                   "Descuento" to m3.dto3,
                   "Comentarios" to m3.comentarios3,
               )





               //añade a la base de datos
               val nuevaVisita = db
                   .collection("vendedores")
               nuevaVisita.add(vendedor)

                   .addOnSuccessListener {documentReference ->
                       Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                      val marcaM1 = db
                          .collection("vendedores").document("${documentReference.id}")
                          .collection("Marcas Presentadas").document("Marcas1")
                       marcaM1.set(marca1)
                       val marcaM2 = db
                           .collection("vendedores").document("${documentReference.id}")
                           .collection("Marcas Presentadas").document("Marcas2")
                       marcaM2.set(marca2)
                       val marcaM3 = db
                           .collection("vendedores").document("${documentReference.id}")
                           .collection("Marcas Presentadas").document("Marcas3")
                       marcaM3.set(marca3)





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

        binding.editButton.setOnClickListener{
            db.collection("vendedores").document(email).get().addOnSuccessListener {
                binding.clienteET.setText(it.get("cliente")as String?)
                binding.comentariosMultiLine.setText(it.get("comentarios")as String?)
            }
            //pantalla dfe aviso de guardado
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Guardado")
            builder.setMessage("Se ha modificado el registro")
            builder.setPositiveButton("Aceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        binding.deleteButton.setOnClickListener{
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
        binding.anadirMarcaButton.setOnClickListener{

            binding.anadirMarcaButton.visibility = View.GONE
            binding.marca1Container.isVisible= true

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
        binding.anadirMarca1Button.setOnClickListener{

            binding.anadirMarca1Button.visibility = View.GONE
            binding.marca2Container.isVisible= true

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
        binding.anadirMarca2Button2.setOnClickListener{
            binding.anadirMarca2Button2.visibility = View.GONE
            binding.marca3Container.isVisible= true

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
        binding.clienteET.setText("")
        binding.comentariosMultiLine.setText("")

        binding.marca1Container.isVisible= false
        binding.marca2Container.isVisible= false
        binding.marca3Container.isVisible= false
    }
}


