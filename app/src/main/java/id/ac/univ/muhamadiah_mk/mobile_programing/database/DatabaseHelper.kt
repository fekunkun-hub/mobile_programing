package id.ac.univ.muhamadiah_mk.mobile_programing.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import id.ac.univ.muhamadiah_mk.mobile_programing.model.Paket
import id.ac.univ.muhamadiah_mk.mobile_programing.model.Pemesanan

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "rastudio_v4.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE users (
                id_user INTEGER PRIMARY KEY AUTOINCREMENT,
                nama TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                no_hp TEXT,
                role TEXT NOT NULL DEFAULT 'user'
            )
        """)

        db.execSQL("""
            CREATE TABLE paket (
                id_paket INTEGER PRIMARY KEY AUTOINCREMENT,
                nama_paket TEXT NOT NULL,
                kategori TEXT,
                deskripsi TEXT,
                harga TEXT,
                durasi TEXT,
                location TEXT,
                rating TEXT,
                reviews TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE favorites (
                id_favorite INTEGER PRIMARY KEY AUTOINCREMENT,
                id_paket INTEGER UNIQUE,
                FOREIGN KEY (id_paket) REFERENCES paket(id_paket)
            )
        """)

        db.execSQL("""
            CREATE TABLE pemesanan (
                id_pemesanan INTEGER PRIMARY KEY AUTOINCREMENT,
                id_paket INTEGER,
                nama_paket TEXT,
                harga TEXT,
                tanggal TEXT,
                waktu TEXT,
                lokasi TEXT,
                catatan TEXT,
                status TEXT,
                FOREIGN KEY (id_paket) REFERENCES paket(id_paket)
            )
        """)

        insertInitialPaket(db)
        
        // Insert Admin Default
        val adminCv = ContentValues()
        adminOf("nama" to "Admin", "email" to "admin@rastudio.com", "password" to "admin123", "role" to "admin").forEach { (k, v) ->
            adminCv.put(k, v)
        }
        db.insert("users", null, adminCv)
    }

    private fun adminOf(vararg pairs: Pair<String, String>) = pairs.toMap()

    private fun insertInitialPaket(db: SQLiteDatabase) {
        val packages = listOf(
            arrayOf("1", "Studio Photography", "Studio", "Professional studio session with lights.", "Rp 1.200K", "2 Hours", "Indoor Session", "4.9", "128 reviews"),
            arrayOf("2", "Wedding / Pernikahan", "Wedding", "Full day wedding coverage.", "Rp 5.000K", "Full Day", "On-site", "5.0", "85 reviews"),
            arrayOf("3", "Graduation / Wisuda", "Wisuda", "High quality graduation portraits.", "Rp 800K", "1 Hour", "Studio", "4.8", "210 reviews"),
            arrayOf("4", "Tasmiyah / Aqiqah", "Event", "Documenting naming ceremony.", "Rp 1.500K", "3 Hours", "Home", "4.9", "42 reviews"),
            arrayOf("5", "Sunatan / Khitanan", "Event", "Professional khitanan session.", "Rp 1.000K", "2 Hours", "Home", "4.7", "30 reviews")
        )
        
        for (p in packages) {
            val cv = ContentValues()
            cv.put("id_paket", p[0])
            cv.put("nama_paket", p[1])
            cv.put("kategori", p[2])
            cv.put("deskripsi", p[3])
            cv.put("harga", p[4])
            cv.put("durasi", p[5])
            cv.put("location", p[6])
            cv.put("rating", p[7])
            cv.put("reviews", p[8])
            db.insert("paket", null, cv)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS favorites")
        db.execSQL("DROP TABLE IF EXISTS paket")
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS pemesanan")
        onCreate(db)
    }

    // AUTH METHODS
    fun registerUser(nama: String, email: String, password: String, noHp: String): Long {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put("nama", nama)
        cv.put("email", email)
        cv.put("password", password)
        cv.put("no_hp", noHp)
        cv.put("role", "user")
        return db.insert("users", null, cv)
    }

    fun checkLogin(email: String, password: String): android.database.Cursor? {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", arrayOf(email, password))
    }

    fun checkEmailExists(email: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // FAVORITE METHODS
    fun addFavorite(idPaket: Int): Long {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put("id_paket", idPaket)
        return db.insertWithOnConflict("favorites", null, cv, SQLiteDatabase.CONFLICT_IGNORE)
    }

    fun removeFavorite(idPaket: Int): Int {
        val db = writableDatabase
        return db.delete("favorites", "id_paket = ?", arrayOf(idPaket.toString()))
    }

    fun isFavorite(idPaket: Int): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM favorites WHERE id_paket = ?", arrayOf(idPaket.toString()))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun getFavoritePackages(): List<Paket> {
        val list = mutableListOf<Paket>()
        val db = readableDatabase
        val query = "SELECT * FROM paket WHERE id_paket IN (SELECT id_paket FROM favorites)"
        val cursor = db.rawQuery(query, null)
        
        if (cursor.moveToFirst()) {
            do {
                list.add(Paket(
                    id = cursor.getInt(0),
                    nama = cursor.getString(1) ?: "",
                    kategori = cursor.getString(2) ?: "",
                    deskripsi = cursor.getString(3) ?: "",
                    harga = cursor.getString(4) ?: "",
                    duration = cursor.getString(5) ?: "",
                    location = cursor.getString(6) ?: "",
                    rating = cursor.getString(7) ?: "",
                    reviews = cursor.getString(8) ?: "",
                    includes = emptyList()
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getFavoritesCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM favorites", null)
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }

    // BOOKING METHODS
    fun addBooking(p: Pemesanan): Long {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put("id_paket", p.paketId)
        cv.put("nama_paket", p.namaPaket)
        cv.put("harga", p.harga)
        cv.put("tanggal", p.tanggal)
        cv.put("waktu", p.waktu)
        cv.put("lokasi", p.lokasi)
        cv.put("catatan", p.catatan)
        cv.put("status", p.status)
        return db.insert("pemesanan", null, cv)
    }

    fun getBookingsCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM pemesanan", null)
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }

    fun getActiveBookingsCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM pemesanan WHERE status = 'Active' OR status = 'Pending' OR status = 'Dipesan'", null)
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }
}
