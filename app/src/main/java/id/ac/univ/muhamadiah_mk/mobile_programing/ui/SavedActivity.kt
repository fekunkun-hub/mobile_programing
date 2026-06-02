package id.ac.univ.muhamadiah_mk.mobile_programing.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.MainActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.R
import id.ac.univ.muhamadiah_mk.mobile_programing.database.DatabaseHelper
import id.ac.univ.muhamadiah_mk.mobile_programing.model.Paket

class SavedActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var containerSavedItems: LinearLayout
    private lateinit var layoutEmpty: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        dbHelper = DatabaseHelper(this)
        containerSavedItems = findViewById(R.id.containerSavedItems)
        layoutEmpty = findViewById(R.id.layoutEmpty)

        setupNavigation()
        loadSavedItems()
    }

    private fun setupNavigation() {
        findViewById<LinearLayout>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.navServices).setOnClickListener {
            startActivity(Intent(this, ServicesActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.btnExplore).setOnClickListener {
            startActivity(Intent(this, ServicesActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.navProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }

    private fun loadSavedItems() {
        val favoritePackages = dbHelper.getFavoritePackages()

        if (favoritePackages.isEmpty()) {
            layoutEmpty.visibility = View.VISIBLE
            containerSavedItems.visibility = View.GONE
        } else {
            layoutEmpty.visibility = View.GONE
            containerSavedItems.visibility = View.VISIBLE
            containerSavedItems.removeAllViews()

            favoritePackages.forEach { paket ->
                val itemView = layoutInflater.inflate(R.layout.item_saved, containerSavedItems, false)

                itemView.findViewById<TextView>(R.id.tvSavedTitle).text = paket.nama
                itemView.findViewById<TextView>(R.id.tvSavedPrice).text = paket.harga
                itemView.findViewById<TextView>(R.id.tvSavedLocation).text = "📍 ${paket.location}"
                itemView.findViewById<TextView>(R.id.tvSavedDuration).text = "🕒 ${paket.duration}"

                itemView.findViewById<TextView>(R.id.btnViewDetailsSaved).setOnClickListener {
                    val intent = Intent(this, ServiceDetailActivity::class.java)
                    intent.putExtra("DATA_PAKET", paket)
                    startActivity(intent)
                }

                itemView.findViewById<ImageView>(R.id.btnRemoveSaved).setOnClickListener {
                    dbHelper.removeFavorite(paket.id)
                    Toast.makeText(this, "${paket.nama} dihapus dari Saved", Toast.LENGTH_SHORT).show()
                    loadSavedItems() // Refresh list
                }

                containerSavedItems.addView(itemView)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadSavedItems() // Reload data when returning to this activity
    }
}
