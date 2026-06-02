package id.ac.univ.muhamadiah_mk.mobile_programing.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.R
import id.ac.univ.muhamadiah_mk.mobile_programing.database.DatabaseHelper
import id.ac.univ.muhamadiah_mk.mobile_programing.model.Pemesanan
import java.util.*

class BookingActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etDate: EditText
    private lateinit var etNotes: EditText
    private lateinit var tvTotalPrice: TextView
    private lateinit var btnConfirm: TextView
    
    private lateinit var itemBasic: LinearLayout
    private lateinit var itemPremium: LinearLayout
    private lateinit var itemDeluxe: LinearLayout
    private lateinit var rbBasic: RadioButton
    private lateinit var rbPremium: RadioButton
    private lateinit var rbDeluxe: RadioButton

    private var selectedPackageId = 102
    private var selectedPackageName = "Premium Package"
    private var selectedPrice = "Rp 1.500.000"
    private var selectedTime = "09:00 AM"
    private var selectedLocation = "RAStudio Main - Jakarta Selatan"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        dbHelper = DatabaseHelper(this)

        initViews()
        setupListeners()
        handleIncomingIntent()
    }

    private fun initViews() {
        etDate = findViewById(R.id.etDate)
        etNotes = findViewById(R.id.etNotes)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        btnConfirm = findViewById(R.id.btnConfirm)
        
        itemBasic = findViewById(R.id.itemBasic)
        itemPremium = findViewById(R.id.itemPremium)
        itemDeluxe = findViewById(R.id.itemDeluxe)
        rbBasic = findViewById(R.id.rbBasic)
        rbPremium = findViewById(R.id.rbPremium)
        rbDeluxe = findViewById(R.id.rbDeluxe)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }
    }

    private fun handleIncomingIntent() {
        val packageId = intent.getIntExtra("SELECTED_PACKAGE_ID", -1)
        if (packageId != -1) {
            updatePackageSelection(when(packageId) {
                101 -> R.id.rbBasic
                103 -> R.id.rbDeluxe
                else -> R.id.rbPremium
            })
        }
    }

    private fun setupListeners() {
        // Date Picker
        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val date = "${month + 1}/$dayOfMonth/$year"
                etDate.setText(date)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Package Selection
        val packageClick = View.OnClickListener { v ->
            val id = when(v.id) {
                R.id.itemBasic, R.id.rbBasic -> R.id.rbBasic
                R.id.itemDeluxe, R.id.rbDeluxe -> R.id.rbDeluxe
                else -> R.id.rbPremium
            }
            updatePackageSelection(id)
        }

        itemBasic.setOnClickListener(packageClick)
        rbBasic.setOnClickListener(packageClick)
        itemPremium.setOnClickListener(packageClick)
        rbPremium.setOnClickListener(packageClick)
        itemDeluxe.setOnClickListener(packageClick)
        rbDeluxe.setOnClickListener(packageClick)

        // Time Slot Selection
        val gridTime = findViewById<View>(R.id.gridTime) as? ViewGroup
        gridTime?.let { container ->
            for (i in 0 until container.childCount) {
                val child = container.getChildAt(i)
                if (child is TextView) {
                    child.setOnClickListener {
                        // Reset all in this container
                        for (j in 0 until container.childCount) {
                            val v = container.getChildAt(j)
                            if (v is TextView) {
                                val pL = v.paddingLeft
                                val pT = v.paddingTop
                                val pR = v.paddingRight
                                val pB = v.paddingBottom
                                v.setBackgroundResource(R.drawable.bg_search)
                                v.setPadding(pL, pT, pR, pB)
                                v.setTextColor(getColor(R.color.white))
                            }
                        }
                        // Highlight selected
                        val cpL = child.paddingLeft
                        val cpT = child.paddingTop
                        val cpR = child.paddingRight
                        val cpB = child.paddingBottom
                        child.setBackgroundResource(R.drawable.bg_button_orange)
                        child.setPadding(cpL, cpT, cpR, cpB)
                        child.setTextColor(getColor(R.color.black))
                        selectedTime = child.text.toString()
                    }
                }
            }
        }

        // Location Selection
        val llLocations = findViewById<LinearLayout>(R.id.llLocations)
        for (i in 0 until llLocations.childCount) {
            val child = llLocations.getChildAt(i)
            if (child is TextView) {
                child.setOnClickListener {
                    for (j in 0 until llLocations.childCount) {
                        val v = llLocations.getChildAt(j)
                        if (v is TextView) {
                            val pL = v.paddingLeft
                            val pT = v.paddingTop
                            val pR = v.paddingRight
                            val pB = v.paddingBottom
                            v.setBackgroundResource(R.drawable.bg_search)
                            v.setPadding(pL, pT, pR, pB)
                        }
                    }
                    val cpL = child.paddingLeft
                    val cpT = child.paddingTop
                    val cpR = child.paddingRight
                    val cpB = child.paddingBottom
                    child.setBackgroundResource(R.drawable.bg_card_selected)
                    child.setPadding(cpL, cpT, cpR, cpB)
                    selectedLocation = child.text.toString()
                }
            }
        }

        // Confirm Booking
        btnConfirm.setOnClickListener {
            val date = etDate.text.toString()
            if (date.isEmpty()) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val booking = Pemesanan(
                paketId = selectedPackageId,
                namaPaket = selectedPackageName,
                harga = selectedPrice,
                tanggal = date,
                waktu = selectedTime,
                lokasi = selectedLocation,
                catatan = etNotes.text.toString()
            )

            val result = dbHelper.addBooking(booking)
            if (result != -1L) {
                Toast.makeText(this, "Booking Successful!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Booking Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePackageSelection(checkedId: Int) {
        // Store current paddings
        val bL = itemBasic.paddingLeft; val bT = itemBasic.paddingTop; val bR = itemBasic.paddingRight; val bB = itemBasic.paddingBottom
        val pL = itemPremium.paddingLeft; val pT = itemPremium.paddingTop; val pR = itemPremium.paddingRight; val pB = itemPremium.paddingBottom
        val dL = itemDeluxe.paddingLeft; val dT = itemDeluxe.paddingTop; val dR = itemDeluxe.paddingRight; val dB = itemDeluxe.paddingBottom

        // Reset radio buttons
        rbBasic.isChecked = checkedId == R.id.rbBasic
        rbPremium.isChecked = checkedId == R.id.rbPremium
        rbDeluxe.isChecked = checkedId == R.id.rbDeluxe

        // Update backgrounds and restore paddings
        itemBasic.setBackgroundResource(if (checkedId == R.id.rbBasic) R.drawable.bg_card_selected else R.drawable.bg_card_package)
        itemBasic.setPadding(bL, bT, bR, bB)
        
        itemPremium.setBackgroundResource(if (checkedId == R.id.rbPremium) R.drawable.bg_card_selected else R.drawable.bg_card_package)
        itemPremium.setPadding(pL, pT, pR, pB)
        
        itemDeluxe.setBackgroundResource(if (checkedId == R.id.rbDeluxe) R.drawable.bg_card_selected else R.drawable.bg_card_package)
        itemDeluxe.setPadding(dL, dT, dR, dB)

        when (checkedId) {
            R.id.rbBasic -> {
                selectedPackageId = 101
                selectedPackageName = "Basic Package"
                selectedPrice = "Rp 500.000"
            }
            R.id.rbPremium -> {
                selectedPackageId = 102
                selectedPackageName = "Premium Package"
                selectedPrice = "Rp 1.500.000"
            }
            R.id.rbDeluxe -> {
                selectedPackageId = 103
                selectedPackageName = "Deluxe Package"
                selectedPrice = "Rp 3.000.000"
            }
        }
        tvTotalPrice.text = selectedPrice
    }
}
