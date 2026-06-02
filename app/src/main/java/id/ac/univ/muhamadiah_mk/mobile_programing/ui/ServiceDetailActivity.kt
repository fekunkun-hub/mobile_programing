package id.ac.univ.muhamadiah_mk.mobile_programing.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.R
import id.ac.univ.muhamadiah_mk.mobile_programing.model.Paket

class ServiceDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_detail)

        val paket = intent.getSerializableExtra("DATA_PAKET") as? Paket

        val tvTitle = findViewById<TextView>(R.id.tvDetailTitle)
        val tvRating = findViewById<TextView>(R.id.tvDetailRating)
        val tvPrice = findViewById<TextView>(R.id.tvDetailPrice)
        val tvDesc = findViewById<TextView>(R.id.tvDetailDesc)
        val tvLocation = findViewById<TextView>(R.id.tvDetailLocation)
        val tvDuration = findViewById<TextView>(R.id.tvDetailDuration)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val containerIncludes = findViewById<LinearLayout>(R.id.containerIncludes)

        paket?.let {
            tvTitle.text = it.nama
            tvRating.text = "${it.rating} (${it.reviews})"
            tvPrice.text = it.harga
            tvDesc.text = it.deskripsi
            tvLocation.text = it.location
            tvDuration.text = it.duration

            // Clear dummy data and add dynamic includes
            containerIncludes.removeAllViews()
            it.includes.forEach { item ->
                val itemView = layoutInflater.inflate(R.layout.item_include, containerIncludes, false)
                itemView.findViewById<TextView>(R.id.tvIncludeText).text = item
                containerIncludes.addView(itemView)
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}