package id.ac.univ.muhamadiah_mk.mobile_programing.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.R
import java.net.URLEncoder

class WhatsappOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whatsapp_order)

        findViewById<ImageView>(R.id.btnBackWa).setOnClickListener {
            finish()
        }

        setupOptions()
    }

    private fun setupOptions() {
        val options = mapOf(
            R.id.btnOptWaWedding to "Halo Admin RAStudio, saya ingin tanya/order layanan Wedding.",
            R.id.btnOptWaWisuda to "Halo Admin RAStudio, saya ingin tanya/order layanan Wisuda.",
            R.id.btnOptWaPasPhoto to "Halo Admin RAStudio, saya ingin tanya/order layanan Pas Photo.",
            R.id.btnOptWaCetak to "Halo Admin RAStudio, saya ingin tanya/order layanan Cuci Cetak.",
            R.id.btnOptWaEdit to "Halo Admin RAStudio, saya ingin tanya/order layanan Edit Photo.",
            R.id.btnOptWaLainnya to "Halo Admin RAStudio, saya ingin tanya/konsultasi layanan lainnya."
        )

        options.forEach { (viewId, message) ->
            findViewById<LinearLayout>(viewId)?.setOnClickListener {
                openWhatsApp(message)
            }
        }
    }

    private fun openWhatsApp(message: String) {
        val phoneNumber = "+6281234567890" // Ganti dengan nomor WhatsApp admin asli
        try {
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=" + URLEncoder.encode(message, "UTF-8")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal membuka WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }
}