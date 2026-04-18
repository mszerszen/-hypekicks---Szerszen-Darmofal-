package com.example.hypekicksds

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hypekicksds.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var sneakersList: MutableList<Sneaker>
    lateinit var adapter: SneakerAdapter
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sneakersList = mutableListOf()

        adapter = SneakerAdapter(this, sneakersList)
        binding.sneakersGridView.adapter = adapter

        fetchSneakersFromCloud()
    }
    private fun fetchSneakersFromCloud() {
        db.collection("sneakers")
            .get()
            .addOnSuccessListener { documents ->
                sneakersList.clear()

                for (document in documents) {
                    val name = document.getString("name") ?: "Brak nazwy"
                    val price = document.getDouble("price") ?: 0.0
                    val description = document.getString("description") ?: "Brak opisu"
                    val imageUrl = document.getString("imageUrl") ?: ""
                    val stock = document.getLong("stock")?.toInt() ?: 0

                    val sneaker = Sneaker(name, price, description, imageUrl, stock)
                    sneakersList.add(sneaker)
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("FIREBASE_ERROR", "Błąd pobierania danych: ", exception)
                Toast.makeText(this, "Nie udało się pobrać danych!", Toast.LENGTH_LONG).show()
            }
    }

}