package com.example.aljabermall.adapters

interface OnCartListener {
    fun removeFromCart(pid: Int,postion:Int)
    fun updateCartItem(pid: Int, quantity: Int,order_idBody:String)
}