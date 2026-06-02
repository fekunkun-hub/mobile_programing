package id.ac.univ.muhamadiah_mk.mobile_programing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.database.DatabaseHelper
import id.ac.univ.muhamadiah_mk.mobile_programing.model.Paket
import id.ac.univ.muhamadiah_mk.mobile_programing.ui.BookingActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.ui.NotificationsActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.ui.PackageDetailActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.ui.ProfileActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.ui.SavedActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.ui.ServicesActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    private lateinit var btnBasic: TextView
    private lateinit var btnPremium: TextView
    private lateinit var btnDeluxe: TextView
    private lateinit var btnBookNow: TextView

    private lateinit var tvHomeGreeting: TextView
    private lateinit var tvHomeInitials: TextView
    private lateinit var ivHomeNotification: ImageView

    private lateinit var navHome: LinearLayout
    private lateinit var navServices: LinearLayout
    private lateinit var navSaved: LinearLayout
    private lateinit var navProfile: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        
        initViews()
        loadUserData()
        setupClickListeners()
    }

    private fun initViews() {
        btnBasic = findViewById(R.id.btnBasic)
        btnPremium = findViewById(R.id.btnPremium)
        btnDeluxe = findViewById(R.id.btnDeluxe)
        btnBookNow = findViewById(R.id.btnBookNow)

        tvHomeGreeting = findViewById(R.id.tvHomeGreeting)
        tvHomeInitials = findViewById(R.id.tvHomeInitials)
        ivHomeNotification = findViewById(R.id.ivHomeNotification)

        navHome = findViewById(R.id.navHome)
        navServices = findViewById(R.id.navServices)
        navSaved = findViewById(R.id.navSaved)
        navProfile = findViewById(R.id.navProfile)
    }

    private fun loadUserData() {
        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("user_name", "User") ?: "User"

        // Set Dynamic Greeting
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val greeting = when (hour) {
            in 0..11 -> "Good Morning ☀️"
            in 12..15 -> "Good Afternoon ☀️"
            in 16..20 -> "Good Evening 🌙"
            else -> "Good Night 🌙"
        }
        
        tvHomeGreeting.text = "$greeting, $userName"

        // Set Initials
        val initials = userName.split(" ")
            .filter { it.isNotEmpty() }
            .map { it[0].uppercaseChar() }
            .take(2)
            .joinToString("")
        tvHomeInitials.text = if (initials.isNotEmpty()) initials else "U"
    }

    private fun setupClickListeners() {
        btnBookNow.setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }

        ivHomeNotification.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        tvHomeInitials.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        btnBasic.setOnClickListener {
            val paket = Paket(
                id = 101,
                nama = "Basic Package",
                tagline = "Perfect for personal use",
                harga = "Rp 500.000",
                footerDesc = "Affordable package for portraits, graduation, and simple events.",
                includes = listOf(
                    "1 Photographer",
                    "1–2 Hours Session",
                    "1 Location",
                    "20–30 Edited Photos",
                    "All Raw Photos (Digital)"
                ),
                perfectFor = listOf("Individual portraits and headshots", "Graduation photos"),
                colorRes = R.color.package_basic
            )
            openDetail(paket)
        }

        btnPremium.setOnClickListener {
            val paket = Paket(
                id = 102,
                nama = "Premium Package",
                tagline = "Most chosen package",
                harga = "Rp 1.500.000",
                footerDesc = "Complete experience with professional editing and printed memories.",
                includes = listOf(
                    "1–2 Photographers",
                    "4–6 Hours Session",
                    "Up to 2 Locations",
                    "50–80 Edited Photos",
                    "All Raw Photos (Cloud)",
                    "1 Standard Photobook",
                    "1 Framed Photo (16R)"
                ),
                perfectFor = listOf("Couples and small families", "Birthday parties and small events"),
                colorRes = R.color.package_premium
            )
            openDetail(paket)
        }

        btnDeluxe.setOnClickListener {
            val paket = Paket(
                id = 103,
                nama = "Deluxe Package",
                tagline = "Ultimate experience",
                harga = "Rp 3.000.000",
                footerDesc = "Luxury package with premium editing, exclusive album and cinematic teaser.",
                includes = listOf(
                    "2 Photographers + Assistant",
                    "Full Day (Up to 10 Hours)",
                    "Unlimited Locations",
                    "120+ Edited Photos",
                    "Premium Leather Photobook",
                    "2 Framed Photos",
                    "Cinematic Video Teaser",
                    "Express Delivery"
                ),
                perfectFor = listOf("Weddings and grand events", "Full branding / commercial sessions"),
                colorRes = R.color.package_deluxe
            )
            openDetail(paket)
        }

        navServices.setOnClickListener {
            startActivity(Intent(this, ServicesActivity::class.java))
        }

        navSaved.setOnClickListener {
            startActivity(Intent(this, SavedActivity::class.java))
        }

        navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun openDetail(paket: Paket) {
        val intent = Intent(this, PackageDetailActivity::class.java)
        intent.putExtra("DATA_PAKET", paket)
        startActivity(intent)
    }
}