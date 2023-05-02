package com.example.aljabermall.models

data class Data(
    val body: String,
    val catagory: String,
    val currency_code: String,
    val fav: Int,
    val images: List<Image>,
    val pid: String,
    val price: Double,
    val related: List<Related>,
    val title: String,
    val vid: String
)