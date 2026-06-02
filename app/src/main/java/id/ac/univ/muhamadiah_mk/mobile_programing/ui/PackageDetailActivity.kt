package id.ac.univ.muhamadiah_mk.mobile_programing.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.R
import id.ac.univ.muhamadiah_mk.mobile_programing.model.Paket

class PackageDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_detail)

        val paket = intent.getSerializableExtra("DATA_PAKET") as? Paket

        val btnBack = findViewById<ImageView>(R.id.btnBackPackage)
        val tvType = findViewById<TextView>(R.id.tvDetailPackageType)
        val tvTagline = findViewById<TextView>(R.id.tvDetailPackageTagline)
        val tvPrice = findViewById<TextView>(R.id.tvDetailPackagePrice)
        val tvFooter = findViewById<TextView>(R.id.tvDetailPackageFooter)
        val containerIncludes = findViewById<LinearLayout>(R.id.containerDetailIncludes)
        val btnChoose = findViewById<Button>(R.id.btnChooseThisPackage)

        paket?.let {
            tvType.text = it.nama.uppercase()
            tvPrice.text = it.harga
            
            // Adjust colors and texts based on package
            when (it.id) {
                101 -> { // Basic
                    tvType.setTextColor(getColor(R.color.package_basic))
                    btnChoose.backgroundTintList = getColorStateList(R.color.package_basic)
                    tvTagline.text = "Perfect for personal use"
                    tvFooter.text = "Affordable package for portraits, graduation, and simple events."
                }
                102 -> { // Premium
                    tvType.setTextColor(getColor(R.color.package_premium))
                    btnChoose.backgroundTintList = getColorStateList(R.color.package_premium)
                    btnChoose.setTextColor(getColor(R.color.black))
                    tvTagline.text = "Most chosen package"
                    tvFooter.text = "Complete experience with professional editing and printed memories."
                }
                103 -> { // Deluxe
                    tvType.setTextColor(getColor(R.color.package_deluxe))
                    btnChoose.backgroundTintList = getColorStateList(R.color.package_deluxe)
                    tvTagline.text = "Ultimate experience"
                    tvFooter.text = "Luxury package with premium editing, exclusive album and cinematic teaser."
                }
            }

            // Load Includes
            containerIncludes.removeAllViews()
            it.includes.forEach { item ->
                val itemView = layoutInflater.inflate(R.layout.item_include, containerIncludes, false)
                itemView.findViewById<TextView>(R.id.tvIncludeText).text = item
                
                // Color the check icon based on package
                val ivCheck = itemView.findViewById<ImageView>(R.id.ivCheck)
                when (it.id) {
                    101 -> ivCheck.setColorFilter(getColor(R.color.package_basic))
                    102 -> ivCheck.setColorFilter(getColor(R.color.package_premium))
                    103 -> ivCheck.setColorFilter(getColor(R.color.package_deluxe))
                }
                
                containerIncludes.addView(itemView)
            }
        }

        btnBack.setOnClickListener { finish() }
        
        btnChoose.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            intent.putExtra("SELECTED_PACKAGE_ID", paket?.id ?: 102)
            startActivity(intent)
        }
    }
}