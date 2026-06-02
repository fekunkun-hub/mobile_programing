package id.ac.univ.muhamadiah_mk.mobile_programing.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.ac.univ.muhamadiah_mk.mobile_programing.MainActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.R
import id.ac.univ.muhamadiah_mk.mobile_programing.database.DatabaseHelper
import id.ac.univ.muhamadiah_mk.mobile_programing.model.Paket
import java.net.URLEncoder

class ServicesActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var servicesContainer: LinearLayout
    private lateinit var etSearch: EditText

    private val allServices = mutableListOf<Paket>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

        dbHelper = DatabaseHelper(this)
        servicesContainer = findViewById(R.id.servicesContainer)
        etSearch = findViewById(R.id.etSearchServices)

        setupNavigation()
        
        // Trigger untuk membuka pilihan WhatsApp
        findViewById<LinearLayout>(R.id.btnOpenWaOptions).setOnClickListener {
            showWhatsAppOptions()
        }

        initializeServicesList()
        renderServices(allServices)

        etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase()
                val filtered = allServices.filter { 
                    it.nama.lowercase().contains(query) || it.kategori.lowercase().contains(query)
                }
                renderServices(filtered)
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    private fun setupNavigation() {
        findViewById<LinearLayout>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
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

    private fun showWhatsAppOptions() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_wa_options, null)
        
        val options = mapOf(
            R.id.optionWaWedding to "Halo Admin RAStudio, saya ingin tanya/order layanan Wedding.",
            R.id.optionWaWisuda to "Halo Admin RAStudio, saya ingin tanya/order layanan Wisuda.",
            R.id.optionWaPasPhoto to "Halo Admin RAStudio, saya ingin tanya/order layanan Pas Photo.",
            R.id.optionWaCetak to "Halo Admin RAStudio, saya ingin tanya/order layanan Cuci Cetak.",
            R.id.optionWaEdit to "Halo Admin RAStudio, saya ingin tanya/order layanan Edit Photo.",
            R.id.optionWaLainnya to "Halo Admin RAStudio, saya ingin tanya/konsultasi layanan lainnya."
        )

        options.forEach { (viewId, message) ->
            view.findViewById<LinearLayout>(viewId).setOnClickListener {
                openWhatsApp(message)
                dialog.dismiss()
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun openWhatsApp(message: String) {
        val phoneNumber = "+6281234567890" // Ganti dengan nomor WhatsApp admin asli
        try {
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=" + URLEncoder.encode(message, "UTF-8")
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal membuka WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeServicesList() {
        allServices.clear()
        
        // 1. RESEPSI PERNIKAHAN
        allServices.add(Paket(
            id = 101, nama = "Resepsi Pernikahan", kategori = "Photo & Shooting", harga = "Rp 3.500.000",
            rating = "5.0", reviews = "120 reviews", location = "On-site / Venue", duration = "Full Day",
            deskripsi = "Dokumentasi profesional untuk hari spesial pernikahan Anda. Mencakup foto dan video cinematic dari awal hingga akhir acara.",
            includes = listOf("2 Photographers & 1 Videographer", "Unlimited Photos", "Video Cinematic & Teaser", "Luxury Album Kolase", "Cetak 16R + Frame")
        ))

        // 2. ULANG TAHUN
        allServices.add(Paket(
            id = 102, nama = "Ulang Tahun", kategori = "Photo & Shooting", harga = "Rp 1.000.000",
            rating = "4.8", reviews = "45 reviews", location = "Home / Venue", duration = "3 Jam",
            deskripsi = "Abadikan momen kebahagiaan perayaan ulang tahun dengan foto berkualitas tinggi.",
            includes = listOf("1 Photographer", "Edited Photos", "Digital Gallery", "Cetak 10R")
        ))

        // 3. SYUKURAN TASMIYAH
        allServices.add(Paket(
            id = 103, nama = "Syukuran Tasmiyah", kategori = "Photo & Shooting", harga = "Rp 800.000",
            rating = "4.9", reviews = "32 reviews", location = "Rumah / Venue", duration = "2 Jam",
            deskripsi = "Layanan foto untuk acara pemberian nama dan aqiqah buah hati tercinta.",
            includes = listOf("1 Photographer", "High Quality Photos", "All Raw Files", "Cetak 10R")
        ))

        // 4. SYUKURAN SUNATAN
        allServices.add(Paket(
            id = 104, nama = "Syukuran Sunatan", kategori = "Photo & Shooting", harga = "Rp 800.000",
            rating = "4.7", reviews = "28 reviews", location = "Rumah / Venue", duration = "2 Jam",
            deskripsi = "Dokumentasi acara syukuran khitanan keluarga dengan hasil yang memuaskan.",
            includes = listOf("1 Photographer", "Liputan Acara", "File Digital", "Cetak 10R")
        ))

        // 5. SYUKURAN PULANG HAJI
        allServices.add(Paket(
            id = 105, nama = "Syukuran Pulang Haji", kategori = "Photo & Shooting", harga = "Rp 1.200.000",
            rating = "4.9", reviews = "15 reviews", location = "Rumah", duration = "3 Jam",
            deskripsi = "Dokumentasi penyambutan kepulangan jamaah haji bersama keluarga besar.",
            includes = listOf("1 Photographer", "Foto Bersama Keluarga Besar", "Digital Files", "Video Dokumentasi")
        ))

        // 6. WISUDA
        allServices.add(Paket(
            id = 106, nama = "Wisuda", kategori = "Photo & Shooting", harga = "Rp 500.000",
            rating = "4.9", reviews = "250 reviews", location = "Studio / Kampus", duration = "1 Jam",
            deskripsi = "Sesi foto wisuda untuk mengabadikan momen kelulusan Anda dengan pose terbaik.",
            includes = listOf("1 Photographer", "Cetak 10R + Bingkai", "File High Res", "Properti Wisuda")
        ))

        // 7. SEMINAR
        allServices.add(Paket(
            id = 107, nama = "Seminar", kategori = "Photo & Shooting", harga = "Rp 1.500.000",
            rating = "4.7", reviews = "20 reviews", location = "Hotel / Aula", duration = "4 Jam",
            deskripsi = "Dokumentasi kegiatan seminar, workshop, atau pelatihan profesional.",
            includes = listOf("Liputan Kegiatan", "Foto Pembicara & Peserta", "File via Drive", "Highlight Video")
        ))

        // 8. PELANTIKAN
        allServices.add(Paket(
            id = 108, nama = "Pelantikan", kategori = "Photo & Shooting", harga = "Rp 1.500.000",
            rating = "4.8", reviews = "10 reviews", location = "Kantor / Instansi", duration = "3 Jam",
            deskripsi = "Dokumentasi resmi acara pelantikan jabatan atau organisasi.",
            includes = listOf("Foto Prosesi Pelantikan", "Foto Pejabat Terlantik", "File Dokumentasi Lengkap")
        ))

        // 9. PERPISAHAN
        allServices.add(Paket(
            id = 109, nama = "Perpisahan", kategori = "Photo & Shooting", harga = "Rp 2.000.000",
            rating = "4.8", reviews = "25 reviews", location = "Sekolah / Venue", duration = "5 Jam",
            deskripsi = "Abadikan momen perpisahan sekolah atau instansi dengan penuh kenangan.",
            includes = listOf("Liputan Panggung", "Foto Group/Kelas", "Video Dokumentasi", "File via Drive")
        ))

        // 10. PHOTO KELUARGA
        allServices.add(Paket(
            id = 110, nama = "Photo Keluarga", kategori = "Photo & Shooting", harga = "Rp 750.000",
            rating = "4.9", reviews = "88 reviews", location = "Studio", duration = "1 Jam",
            deskripsi = "Sesi foto bersama seluruh anggota keluarga untuk kenangan abadi di dinding rumah.",
            includes = listOf("Sesi Studio", "Cetak 12R + Frame", "File Edit & Raw", "Maksimal 6 Orang")
        ))

        // 11. PAS PHOTO
        allServices.add(Paket(
            id = 111, nama = "Pas Photo", kategori = "Digital & Cetak", harga = "Rp 35.000",
            rating = "4.8", reviews = "500+ reviews", location = "Studio", duration = "15 Menit",
            deskripsi = "Pas foto berkualitas untuk kebutuhan resmi seperti Ijazah, KTP, atau Lamaran Kerja.",
            includes = listOf("Cetak 4x6, 3x4, 2x3", "Ganti Background", "Edit Wajah Natural", "File via WhatsApp")
        ))

        // 12. CUCI CETAK
        allServices.add(Paket(
            id = 112, nama = "Cuci Cetak", kategori = "Digital & Cetak", harga = "Mulai Rp 5.000",
            rating = "4.7", reviews = "150 reviews", location = "Studio", duration = "Sesuai Jumlah",
            deskripsi = "Layanan cetak foto dari berbagai media dengan warna yang tajam dan tahan lama.",
            includes = listOf("Kertas Foto Premium", "Warna Akurat", "Berbagai Ukuran (4R - 24R)")
        ))

        // 13. SCAN PHOTO
        allServices.add(Paket(
            id = 113, nama = "Scan Photo", kategori = "Digital & Cetak", harga = "Rp 10.000",
            rating = "4.6", reviews = "40 reviews", location = "Studio", duration = "10 Menit",
            deskripsi = "Ubah foto fisik lama Anda menjadi file digital dengan kualitas resolusi tinggi.",
            includes = listOf("Scan High Res", "Digital Restoration Ringan", "Output JPG/PNG")
        ))

        // 14. GANTI BEGRON (Background)
        allServices.add(Paket(
            id = 114, nama = "Ganti Begron", kategori = "Digital & Cetak", harga = "Rp 20.000",
            rating = "4.9", reviews = "100 reviews", location = "Studio / Online", duration = "20 Menit",
            deskripsi = "Mengganti latar belakang foto Anda sesuai kebutuhan (Merah, Biru, atau Pemandangan).",
            includes = listOf("Potong Rapi (Masking)", "Warna Latar Custom", "File Digital High Res")
        ))

        // 15. GANTI PAKAI JAS
        allServices.add(Paket(
            id = 115, nama = "Ganti Pakai Jas", kategori = "Digital & Cetak", harga = "Rp 30.000",
            rating = "4.9", reviews = "75 reviews", location = "Studio / Online", duration = "30 Menit",
            deskripsi = "Layanan editing untuk menambahkan pakaian jas atau kemeja secara digital pada foto Anda.",
            includes = listOf("Manipulasi Jas Digital", "Hasil Presisi & Natural", "Pilihan Model Jas", "File Digital")
        ))

        // 16. SPANDUK
        allServices.add(Paket(
            id = 116, nama = "Spanduk", kategori = "Digital & Cetak", harga = "Rp 30.000/meter",
            rating = "4.6", reviews = "60 reviews", location = "Studio", duration = "1-2 Hari",
            deskripsi = "Jasa desain dan cetak spanduk atau banner untuk promosi atau acara.",
            includes = listOf("Bahan Flexy Standard/High", "Cetak Full Color", "Gratis Desain Ringan")
        ))

        // 17. UNDANGAN
        allServices.add(Paket(
            id = 117, nama = "Undangan", kategori = "Digital & Cetak", harga = "Mulai Rp 2.000",
            rating = "4.7", reviews = "90 reviews", location = "Studio", duration = "3-7 Hari",
            deskripsi = "Cetak undangan pernikahan, ulang tahun, atau acara lainnya dengan berbagai desain.",
            includes = listOf("Banyak Pilihan Desain", "Kertas Art Paper/Kartu TIK", "Minimal Order 100 Pcs")
        ))

        // 18. KARTU NAMA
        allServices.add(Paket(
            id = 118, nama = "Kartu Nama", kategori = "Digital & Cetak", harga = "Rp 50.000/box",
            rating = "4.8", reviews = "110 reviews", location = "Studio", duration = "1 Hari",
            deskripsi = "Cetak kartu nama profesional untuk branding diri atau bisnis Anda.",
            includes = listOf("Isi 100 Lembar", "Bahan Art Carton 260gr", "Box Kartu Nama", "Cetak 1 Sisi/2 Sisi")
        ))

        // 19. EDIT PHOTO
        allServices.add(Paket(
            id = 119, nama = "Edit Photo", kategori = "Digital & Cetak", harga = "Mulai Rp 25.000",
            rating = "4.8", reviews = "80 reviews", location = "Studio / Online", duration = "1-2 Jam",
            deskripsi = "Layanan editing foto profesional (Retouching, Color Grading, Restorasi Foto Lama).",
            includes = listOf("Retouch Wajah/Bodi", "Pencahayaan & Warna", "Restorasi Foto Rusak")
        ))

        // 20. EDIT VIDEO DVD
        allServices.add(Paket(
            id = 120, nama = "Edit Video DVD", kategori = "Digital & Cetak", harga = "Mulai Rp 250.000",
            rating = "4.7", reviews = "30 reviews", location = "Studio", duration = "2-5 Hari",
            deskripsi = "Jasa editing video untuk disimpan ke dalam keping DVD (Dokumentasi Acara, Film Pendek).",
            includes = listOf("Video Cutting & Mixing", "BGM/Music Background", "Menu DVD & Case", "Kualitas SD/HD")
        ))

        // 21. COPY VIDEO DVD/VCD
        allServices.add(Paket(
            id = 121, nama = "Copy Video DVD/VCD", kategori = "Digital & Cetak", harga = "Rp 25.000/disc",
            rating = "4.5", reviews = "25 reviews", location = "Studio", duration = "1 Jam",
            deskripsi = "Layanan penggandaan (duplikasi) data video ke dalam keping VCD atau DVD.",
            includes = listOf("Burning High Speed", "Keping DVD/VCD R", "Amplop Kertas")
        ))

        // 22. TRANSFER FILE KE VCD
        allServices.add(Paket(
            id = 122, nama = "Transfer File ke VCD", kategori = "Digital & Cetak", harga = "Rp 35.000",
            rating = "4.4", reviews = "15 reviews", location = "Studio", duration = "1 Jam",
            deskripsi = "Memindahkan file video digital Anda agar dapat diputar di VCD Player lama.",
            includes = listOf("Format Conversion to DAT", "Burning VCD", "Kompatibilitas Luas")
        ))

        // 23. PRINT SURAT
        allServices.add(Paket(
            id = 123, nama = "Print Surat", kategori = "Digital & Cetak", harga = "Rp 1.000/lembar",
            rating = "4.9", reviews = "200+ reviews", location = "Studio", duration = "5 Menit",
            deskripsi = "Layanan cetak dokumen, surat lamaran, atau tugas kantor/sekolah.",
            includes = listOf("Print Hitam Putih / Warna", "Kertas HVS A4/F4", "Hasil Cetak Tajam")
        ))

        // 24. MENGEMBALIKAN DATA YG TERHAPUS
        allServices.add(Paket(
            id = 124, nama = "Recovery Data Memori", kategori = "Digital & Cetak", harga = "Rp 100.000",
            rating = "4.6", reviews = "18 reviews", location = "Studio", duration = "1-3 Jam",
            deskripsi = "Membantu mengembalikan file foto atau video yang hilang atau tidak sengaja terhapus dari memori kamera/HP.",
            includes = listOf("Recovery Foto & Video", "Support SD Card/MMC/Flashdisk", "Data Aman & Rahasia")
        ))

        // 25. DAN LAIN - LAIN
        allServices.add(Paket(
            id = 125, nama = "Layanan Lain-lain", kategori = "Umum", harga = "Hubungi Kami",
            rating = "5.0", reviews = "10 reviews", location = "Studio", duration = "Variatif",
            deskripsi = "Kami juga melayani berbagai kebutuhan digital dan fotografi lainnya yang tidak tertera di daftar.",
            includes = listOf("Konsultasi Fotografi", "Sewa Properti Ringan", "Layanan Custom")
        ))
    }

    private fun renderServices(list: List<Paket>) {
        servicesContainer.removeAllViews()
        val inflater = LayoutInflater.from(this)

        for (paket in list) {
            val cardView = inflater.inflate(R.layout.item_service_card, servicesContainer, false)
            
            val tvName = cardView.findViewById<TextView>(R.id.tvServiceName)
            val tvPrice = cardView.findViewById<TextView>(R.id.tvServicePriceTag)
            val tvCategory = cardView.findViewById<TextView>(R.id.tvServiceCategory)
            val btnDetails = cardView.findViewById<TextView>(R.id.btnViewDetails)
            val btnFav = cardView.findViewById<ImageView>(R.id.btnFavService)
            val ivImage = cardView.findViewById<ImageView>(R.id.ivServiceImage)

            tvName.text = paket.nama
            tvPrice.text = paket.harga
            tvCategory.text = paket.kategori
            
            // Set image placeholder
            ivImage.setImageResource(R.drawable.ic_launcher_background)

            // Favorite handling
            updateHeartUI(btnFav, dbHelper.isFavorite(paket.id))
            btnFav.setOnClickListener {
                if (dbHelper.isFavorite(paket.id)) {
                    dbHelper.removeFavorite(paket.id)
                    updateHeartUI(btnFav, false)
                    Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show()
                } else {
                    dbHelper.addFavorite(paket.id)
                    updateHeartUI(btnFav, true)
                    Toast.makeText(this, "Disimpan ke Favorit", Toast.LENGTH_SHORT).show()
                }
            }

            btnDetails.setOnClickListener {
                val intent = Intent(this, ServiceDetailActivity::class.java)
                intent.putExtra("DATA_PAKET", paket)
                startActivity(intent)
            }

            servicesContainer.addView(cardView)
        }
    }

    private fun updateHeartUI(iv: ImageView, isFav: Boolean) {
        if (isFav) {
            iv.setImageResource(R.drawable.ic_heart)
            iv.setColorFilter(getColor(R.color.orange))
        } else {
            iv.setImageResource(R.drawable.ic_heart_outline)
            iv.setColorFilter(getColor(R.color.white))
        }
    }
}
