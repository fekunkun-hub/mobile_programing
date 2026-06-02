package id.ac.univ.muhamadiah_mk.mobile_programing.model

import java.io.Serializable

data class Paket(
    val id: Int,
    val nama: String,
    val tagline: String = "",
    val harga: String,
    val footerDesc: String = "",
    val includes: List<String> = emptyList(),
    val perfectFor: List<String> = emptyList(),
    val colorRes: Int = 0,
    val kategori: String = "",
    val deskripsi: String = "",
    val duration: String = "",
    val location: String = "",
    val rating: String = "",
    val reviews: String = ""
) : Serializable