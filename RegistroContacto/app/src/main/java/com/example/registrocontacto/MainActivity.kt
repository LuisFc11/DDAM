package com.example.registrocontacto

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etTelefono: EditText
    private lateinit var btnAgregar: Button
    private lateinit var btnContactos: Button
    private lateinit var btnTeclado: Button
    private lateinit var listViewContactos: ListView
    private lateinit var seccionContactos: LinearLayout

    private val contactos = mutableListOf<String>() // Lista para almacenar contactos
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencias a los elementos de la interfaz
        etNombre = findViewById(R.id.etNombre)
        etTelefono = findViewById(R.id.etTelefono)
        btnAgregar = findViewById(R.id.btnAgregar)
        btnContactos = findViewById(R.id.btnContactos)
        btnTeclado = findViewById(R.id.btnTeclado)
        listViewContactos = findViewById(R.id.listViewContactos)
        seccionContactos = findViewById(R.id.seccionContactos)

        // Configurar el adaptador para la lista de contactos
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactos)
        listViewContactos.adapter = adapter

        // Evento para agregar contacto
        btnAgregar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val telefono = etTelefono.text.toString()

            if (nombre.isNotEmpty() && telefono.isNotEmpty()) {
                val contacto = "$nombre - $telefono"
                contactos.add(contacto) // Agregar contacto a la lista
                adapter.notifyDataSetChanged() // Actualizar la lista

                etNombre.text.clear()
                etTelefono.text.clear()

                Toast.makeText(this, "Contacto agregado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Evento para mostrar/ocultar la sección de contactos
        btnContactos.setOnClickListener {
            if (seccionContactos.visibility == View.GONE) {
                seccionContactos.visibility = View.VISIBLE
            } else {
                seccionContactos.visibility = View.GONE
            }
        }

        // Evento para mostrar opciones al presionar un contacto
        listViewContactos.setOnItemClickListener { _, _, position, _ ->
            mostrarOpcionesContacto(position)
        }
    }

    // Método para mostrar opciones de editar o eliminar contacto
    private fun mostrarOpcionesContacto(position: Int) {
        val opciones = arrayOf("Editar", "Eliminar")

        AlertDialog.Builder(this)
            .setTitle("Opciones")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> editarContacto(position)  // Opción Editar
                    1 -> eliminarContacto(position) // Opción Eliminar
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // Método para eliminar contacto
    private fun eliminarContacto(position: Int) {
        contactos.removeAt(position)
        adapter.notifyDataSetChanged()
        Toast.makeText(this, "Contacto eliminado", Toast.LENGTH_SHORT).show()
    }

    // Método para editar contacto
    private fun editarContacto(position: Int) {
        val contactoActual = contactos[position].split(" - ") // Dividir nombre y teléfono
        val nombreActual = contactoActual[0]
        val telefonoActual = contactoActual[1]

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val etNuevoNombre = EditText(this).apply {
            setText(nombreActual)
            hint = "Nuevo Nombre"
        }

        val etNuevoTelefono = EditText(this).apply {
            setText(telefonoActual)
            hint = "Nuevo Teléfono"
        }

        layout.addView(etNuevoNombre)
        layout.addView(etNuevoTelefono)

        AlertDialog.Builder(this)
            .setTitle("Editar Contacto")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoNombre = etNuevoNombre.text.toString()
                val nuevoTelefono = etNuevoTelefono.text.toString()

                if (nuevoNombre.isNotEmpty() && nuevoTelefono.isNotEmpty()) {
                    contactos[position] = "$nuevoNombre - $nuevoTelefono"
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "Contacto actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No se guardaron cambios", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
