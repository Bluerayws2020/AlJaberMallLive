package com.example.aljabermall.adapters

import com.example.aljabermall.models.ProductItems

interface OnProductListener {
    fun addToCart(price: Double?, pid: Int?, quantity: String)
    fun addToFavourite(pid: Int)
    fun removeFromFavourite(favId: Int)
    fun showDetails(product: ProductItems)
}