package com.example.aljabermall.adapters
//com.example.aljabermall

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.aljabermall.R
import com.example.aljabermall.databinding.ItemCartBinding
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.models.CartItemModel
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class CartAdapter(
    private val onCartListener: OnCartListener
) : ListAdapter<CartItemModel, CartAdapter.CartHolder>(DiffUtilCallback) {

    private var language = ""

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        language = HelperUtils.getLang(recyclerView.context)
    }

    object DiffUtilCallback : DiffUtil.ItemCallback<CartItemModel>() {
        override fun areItemsTheSame(oldItem: CartItemModel, newItem: CartItemModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CartItemModel, newItem: CartItemModel): Boolean {
            return oldItem.item_id == newItem.item_id && oldItem.cart_quantity == newItem.cart_quantity
        }

    }

    inner class CartHolder(binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        val productTitle = binding.productTitle
        val productImage = binding.productImage
        val productPrice = binding.productPrice
        val staticProductPrice = binding.staticProductPrice
        val productUnit = binding.productUnit
        private val addItem = binding.addItem
        private val removeItem = binding.removeItem
        private val deleteItem = binding.deleteCartItem
        val itemQuantity = binding.itemQuantity

        val summation = productPrice

        init {
            addItem.setOnClickListener(this)
            removeItem.setOnClickListener(this)
            deleteItem.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            if (bindingAdapterPosition >= 0) {
                var quantity = itemQuantity.text.toString().toInt()
                val order_item_id = currentList[bindingAdapterPosition].order_item_id
               val  order_id  = currentList[bindingAdapterPosition].item_id
                when (v?.id) {
                    R.id.add_item -> {

                            quantity++
                        itemQuantity.text = quantity.toString()
                        val sum : Double = summation.text.toString().toDouble()
                        summation.text = (sum * 2.0).toString()
                        onCartListener.updateCartItem(order_item_id.toInt(), quantity,order_id)
                    }
                    R.id.remove_item -> {
                        if (quantity != 1) {

                                quantity--
                            itemQuantity.text = quantity.toString()
                            onCartListener.updateCartItem(order_item_id.toInt(), quantity,order_id)
                        }
//                            onCartListener.removeFromCart(order_item_id.toInt())
                    }
                    R.id.delete_cart_item -> {

                        onCartListener.removeFromCart(order_item_id.toInt(),bindingAdapterPosition)

                        if (currentList.size == 0){
                            currentList.removeAt(adapterPosition);
                            notifyItemRemoved(adapterPosition);
                        }

                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartHolder(binding)
    }

    override fun onBindViewHolder(holder: CartHolder, position: Int) {
        val cartItem = currentList[position]

        holder.productTitle.text = cartItem.item_name_ar
        holder.productPrice.text = cartItem.total_price
        holder.staticProductPrice.text = cartItem.unit_price

//        holder.itemView.context.getString(
//            R.string.price, cartItem.unit_price.toString()
//        )
        holder.itemQuantity.text = cartItem.cart_quantity.toString()

        holder.productImage.load(HelperUtils.BASE_URL + cartItem.item_image) {
            placeholder(R.drawable.image)
            error(R.drawable.image)
            scale(Scale.FIT)
            crossfade(true)
        }


        val backgroundShapeModel: ShapeAppearanceModel = ShapeAppearanceModel.builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 12F)
            .setTopRightCorner(CornerFamily.ROUNDED, 12F)
            .setBottomLeftCorner(CornerFamily.ROUNDED, 12F)
            .setBottomRightCorner(CornerFamily.ROUNDED, 12F)
            .build()
//        holder.productPrice.background = MaterialShapeDrawable(backgroundShapeModel).apply {
//            fillColor = ColorStateList.valueOf(Color.LTGRAY)
//        }


    }

    override fun getItemCount(): Int {
        return currentList.size
    }


}