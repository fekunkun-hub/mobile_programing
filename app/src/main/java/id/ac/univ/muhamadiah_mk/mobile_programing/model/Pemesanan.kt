package id.ac.univ.muhamadiah_mk.mobile_programing.model

import java.io.Serializable

data class Pemesanan(
    val id: Int = 0,
    val userId: Int = 0,
    val paketId: Int,
    val namaPaket: String,
    val harga: String,
    val tanggal: String,
    val waktu: String,
    val lokasi: String,
    val catatan: String = "",
    val status: String = "Pending"
) : Serializable
