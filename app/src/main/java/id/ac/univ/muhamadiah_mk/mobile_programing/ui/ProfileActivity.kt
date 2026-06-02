package id.ac.univ.muhamadiah_mk.mobile_programing.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.MainActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.R
import id.ac.univ.muhamadiah_mk.mobile_programing.database.DatabaseHelper

class ProfileActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        dbHelper = DatabaseHelper(this)

        val navHome = findViewById<LinearLayout>(R.id.navHome)
        val navServices = findViewById<LinearLayout>(R.id.navServices)
        val navSaved = findViewById<LinearLayout>(R.id.navSaved)
        
        val tvProfileName = findViewById<TextView>(R.id.tvProfileName)
        val tvProfileEmail = findViewById<TextView>(R.id.tvProfileEmail)
        val tvProfileInitials = findViewById<TextView>(R.id.tvProfileInitials)

        val tvCountBookings = findViewById<TextView>(R.id.tvCountBookings)
        val tvCountSaved = findViewById<TextView>(R.id.tvCountSaved)
        val tvCountReviews = findViewById<TextView>(R.id.tvCountReviews)
        val tvActiveBookingsLabel = findViewById<TextView>(R.id.tvActiveBookingsLabel)
        
        val menuEditProfile = findViewById<LinearLayout>(R.id.menuEditProfile)
        val menuBookings = findViewById<LinearLayout>(R.id.menuBookings)
        val menuSaved = findViewById<LinearLayout>(R.id.menuSaved)
        val menuReviews = findViewById<LinearLayout>(R.id.menuReviews)
        val menuNotifications = findViewById<LinearLayout>(R.id.menuNotifications)
        val btnLogout = findViewById<LinearLayout>(R.id.btnLogout)

        // Load User Data from SharedPreferences
        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("user_name", "User") ?: "User"
        val userEmail = sharedPref.getString("user_email", "user@email.com") ?: "user@email.com"

        tvProfileName.text = userName
        tvProfileEmail.text = userEmail
        
        // Set Initials
        val initials = userName.split(" ")
            .filter { it.isNotEmpty() }
            .map { it[0].uppercaseChar() }
            .take(2)
            .joinToString("")
        tvProfileInitials.text = if (initials.isNotEmpty()) initials else "U"

        // Load Stats from Database
        val bookingsCount = dbHelper.getBookingsCount()
        val favoritesCount = dbHelper.getFavoritesCount()
        val activeBookingsCount = dbHelper.getActiveBookingsCount()
        
        tvCountBookings.text = bookingsCount.toString()
        tvCountSaved.text = favoritesCount.toString()
        tvCountReviews.text = "0" // Default 0 for now
        tvActiveBookingsLabel.text = "$activeBookingsCount Active"

        // Navigation (Bottom Nav)
        navHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        navServices.setOnClickListener {
            startActivity(Intent(this, ServicesActivity::class.java))
            finish()
        }

        navSaved.setOnClickListener {
            startActivity(Intent(this, SavedActivity::class.java))
            finish()
        }

        // Menu Interactions
        menuEditProfile.setOnClickListener {
            Toast.makeText(this, "Edit Profile feature coming soon", Toast.LENGTH_SHORT).show()
        }

        menuBookings.setOnClickListener {
            startActivity(Intent(this, MyBookingsActivity::class.java))
        }

        menuSaved.setOnClickListener {
            startActivity(Intent(this, SavedActivity::class.java))
        }

        menuReviews.setOnClickListener {
            startActivity(Intent(this, ReviewsActivity::class.java))
        }

        menuNotifications.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        btnLogout.setOnClickListener {
            // Clear Session
            with(sharedPref.edit()) {
                clear()
                apply()
            }
            
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}