package id.ac.univ.muhamadiah_mk.mobile_programing.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.MainActivity
import id.ac.univ.muhamadiah_mk.mobile_programing.R
import id.ac.univ.muhamadiah_mk.mobile_programing.database.DatabaseHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Persistent Login Check
        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)
        
        if (isLoggedIn) {
            val role = sharedPref.getString("user_role", "user")
            val intent = if (role == "admin") {
                Intent(this, AdminActivity::class.java)
            } else {
                Intent(this, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        dbHelper = DatabaseHelper(this)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnSignIn = findViewById<Button>(R.id.btnLogin)
        val tvSignUp = findViewById<TextView>(R.id.tvSignUp)
        val btnTogglePassword = findViewById<ImageButton>(R.id.btnShowPassword)

        btnTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPassword.transformationMethod = null
                btnTogglePassword.setImageResource(android.R.drawable.ic_menu_view)
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                btnTogglePassword.setImageResource(android.R.drawable.ic_menu_view)
            }
            etPassword.setSelection(etPassword.text.length)
        }

        btnSignIn.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cursor = dbHelper.checkLogin(email, password)
            if (cursor != null && cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex("nama")
                val emailIndex = cursor.getColumnIndex("email")
                val roleIndex = cursor.getColumnIndex("role")
                
                val name = if (nameIndex != -1) cursor.getString(nameIndex) else "User"
                val userEmail = if (emailIndex != -1) cursor.getString(emailIndex) else email
                val role = if (roleIndex != -1) cursor.getString(roleIndex) else "user"
                
                // Save Session
                with(sharedPref.edit()) {
                    putBoolean("is_logged_in", true)
                    putString("user_name", name)
                    putString("user_email", userEmail)
                    putString("user_role", role)
                    apply()
                }

                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                
                val intent = if (role == "admin") {
                    Intent(this, AdminActivity::class.java)
                } else {
                    Intent(this, MainActivity::class.java)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
            cursor?.close()
        }

        tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
