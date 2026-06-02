package id.ac.univ.muhamadiah_mk.mobile_programing.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.MainActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.R
import id.ac.univ.muhamadiah_mk.mobile_programing.database.DatabaseHelper

class MyBookingsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var container: LinearLayout
    private lateinit var layoutEmpty: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bookings)

        dbHelper = DatabaseHelper(this)
        container = findViewById(R.id.containerBookingItems)
        layoutEmpty = findViewById(R.id.layoutEmptyBookings)

        findViewById<LinearLayout>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navServices).setOnClickListener {
            startActivity(Intent(this, ServicesActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navSaved).setOnClickListener {
            startActivity(Intent(this, SavedActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.btnBookNowEmpty).setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }

        loadBookings()
    }

    private fun loadBookings() {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM pemesanan ORDER BY id_pemesanan DESC", null)
        
        container.removeAllViews()
        
        if (cursor.count == 0) {
            layoutEmpty.visibility = View.VISIBLE
            container.visibility = View.GONE
        } else {
            layoutEmpty.visibility = View.GONE
            container.visibility = View.VISIBLE
            
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndexOrThrow("nama_paket"))
                val price = cursor.getString(cursor.getColumnIndexOrThrow("harga"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("tanggal"))
                val time = cursor.getString(cursor.getColumnIndexOrThrow("waktu"))
                val status = cursor.getString(cursor.getColumnIndexOrThrow("status")) ?: "Active"

                val itemView = LayoutInflater.from(this).inflate(R.layout.item_booking, container, false)
                
                itemView.findViewById<TextView>(R.id.tvBookingTitle).text = name
                itemView.findViewById<TextView>(R.id.tvBookingPrice).text = price
                itemView.findViewById<TextView>(R.id.tvBookingDateTime).text = "$date at $time"
                itemView.findViewById<TextView>(R.id.tvBookingStatus).text = status

                container.addView(itemView)
            }
        }
        cursor.close()
    }
}