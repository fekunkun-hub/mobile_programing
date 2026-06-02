package id.ac.univ.muhamadiah_mk.mobile_programing.ui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.R
import id.ac.univ.muhamadiah_mk.mobile_programing.database.DatabaseHelper

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = DatabaseHelper(this)

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnSignUp = findViewById<TextView>(R.id.btnSignUp)
        val tvSignIn = findViewById<TextView>(R.id.tvSignIn)

        btnSignUp.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dbHelper.checkEmailExists(email)) {
                Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = dbHelper.registerUser(name, email, password, phone)
            if (result != -1L) {
                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                finish() // Back to login
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
            }
        }

        tvSignIn.setOnClickListener {
            finish()
        }
    }
}
