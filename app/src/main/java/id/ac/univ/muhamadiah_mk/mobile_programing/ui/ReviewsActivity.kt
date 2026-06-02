package id.ac.univ.muhamadiah_mk.mobile_programing.ui

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.MainActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.R

class ReviewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)

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
    }
}