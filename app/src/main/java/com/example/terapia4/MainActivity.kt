package com.example.terapia4

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    // Lista para guardar los items con la fecha
    private val itemList = mutableListOf<String>()

    // Clave para SharedPreferences
    private val sharedPreferencesKey = "com.example.terapia4.itemList"

    // Declarar listView como propiedad de la clase
    private lateinit var listView: android.widget.ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtener referencias a las vistas
        val spinner = findViewById<android.widget.Spinner>(R.id.spinner)
        val btnSave = findViewById<android.widget.Button>(R.id.btnSave)
        listView = findViewById(R.id.listView)  // Inicializar listView

        // Lista de opciones para el Spinner
        val options = listOf(
            "Ejercicios de fortalecimiento muscular",
            "Estiramientos suaves",
            "Caminar regularmente",
            "Terapia con calor frío",
            "Actividades acuáticas"
        )

        // Crear un adaptador para el Spinner
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        // Crear un adaptador para la ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemList)
        listView.adapter = adapter

        // Cargar los datos guardados al iniciar la aplicación
        loadItemsFromSharedPreferences()

        // Al hacer clic en el botón se agrega el texto a la lista con la fecha
        btnSave.setOnClickListener {
            val selectedItem = spinner.selectedItem.toString()
            if (selectedItem.isNotEmpty()) {
                val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                val itemWithDate = "$selectedItem - $date"
                itemList.add(itemWithDate)
                adapter.notifyDataSetChanged()  // Actualiza la lista
                saveItemsToSharedPreferences()   // Guarda los datos en SharedPreferences
                Toast.makeText(this, "Item agregado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor selecciona un item", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Guardar la lista en SharedPreferences
    private fun saveItemsToSharedPreferences() {
        val sharedPreferences = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("items", itemList.toSet())  // Guarda la lista como un conjunto de strings
        editor.apply()
    }

    // Cargar la lista desde SharedPreferences
    private fun loadItemsFromSharedPreferences() {
        val sharedPreferences = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
        val savedItems = sharedPreferences.getStringSet("items", emptySet()) ?: emptySet()
        itemList.clear()
        itemList.addAll(savedItems)
        (listView.adapter as ArrayAdapter<*>).notifyDataSetChanged()  // Actualiza la ListView
    }
}