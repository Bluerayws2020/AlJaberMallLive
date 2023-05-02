package com.example.aljabermall.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.aljabermall.R
import com.example.aljabermall.databinding.ItemProductBinding
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.ProductItems
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class ProductAdapter(private val onProductListener: OnProductListener,
                     val onChangeCartListener: OnChangeCartListener,
                     val flag:String,
                     private val mContext: Context) :
    ListAdapter<ProductItems, ProductAdapter.ProductHolder>(DiffUtilCallback) {

    private var language = ""

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        language = HelperUtils.getLang(recyclerView.context)
    }

    object DiffUtilCallback : DiffUtil.ItemCallback<ProductItems>() {
        override fun areItemsTheSame(oldItem: ProductItems, newItem: ProductItems): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProductItems, newItem: ProductItems): Boolean {
            return oldItem.id == newItem.id && oldItem.favorite_status == newItem.favorite_status
        }

    }

    inner class ProductHolder(binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

        val productTitle = binding.productTitle
        val productImage = binding.productImage
        val productPrice = binding.productPrice
        private val productCard = binding.productCard
        val favCheck = binding.favouriteClick
        private val quantityCard = binding.quantityCard
        val addToCart = binding.addToCart
        val outOfStock = binding.outOfStockTxt

        private val addItem = binding.addItem
        private val removeItem = binding.removeItem
        private val itemQuantity = binding.itemQuantity


        init {
            productCard.setOnClickListener(this)
            addToCart.setOnClickListener(this)
            addItem.setOnClickListener(this)
            removeItem.setOnClickListener(this)
            favCheck.setOnCheckedChangeListener(this)
        }

        override fun onClick(v: View?) {
            var quantity = itemQuantity.text.toString().toDouble()
            when (v?.id) {
                R.id.product_card -> {
                    onProductListener.showDetails(currentList[bindingAdapterPosition])
                }
                R.id.add_to_cart -> {
                    if (!HelperUtils.isGuest(mContext)) {
                        addToCart.hide()

                        itemQuantity.text = quantity.toString()
                        val pid = currentList[bindingAdapterPosition].category_id
                        val price = currentList[bindingAdapterPosition].item_price

                        onChangeCartListener.changeCartIcon(currentList[bindingAdapterPosition].id)
                        onProductListener.addToCart(price, pid, quantity.toString())

                        quantityCard.show()
                    }
                }
                R.id.add_item -> {
//                    val unitType = currentList[bindingAdapterPosition].unit_type
//                    if (unitType == 1)
//                        quantity += 0.5
//                    else
                    quantity++
                    itemQuantity.text = quantity.toString()
                    val pid = currentList[bindingAdapterPosition].category_id
                    val price = currentList[bindingAdapterPosition].item_price

                    onProductListener.addToCart( price ,pid, quantity.toString())
                }
                R.id.remove_item -> {
                    if (quantity != 0.0) {
//                        val unitType = currentList[bindingAdapterPosition].unit_type
//                        if (unitType == 1)
//                            quantity -= 0.5
//                        else
                        quantity--
                        itemQuantity.text = quantity.toString()
                        val pid = currentList[bindingAdapterPosition].category_id
                        val price = currentList[bindingAdapterPosition].item_price

                        onProductListener.addToCart(price ,pid, quantity.toString())
                    }
                }
            }
        }

        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            if (buttonView?.isPressed == true) {
                if (!HelperUtils.isGuest(buttonView.context)) {
                    if (isChecked) {
                        val pid = currentList[bindingAdapterPosition].id
                        onProductListener.addToFavourite(pid)
                    } else {
                        val favouriteId = currentList[bindingAdapterPosition].favorite_status
                        onProductListener.removeFromFavourite(favouriteId!!)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        val productItem = currentList[position]


        if (language == "ar")
            holder.productTitle.text = productItem.item_name_ar
        else
            holder.productTitle.text = productItem.item_name_ar

Log.d("Check Data", productItem.stock.toString())
        Log.d("Check Datas", productItem.always_in_stock.toString())

        if (productItem.stock == 0.0000 && productItem.always_in_stock == 0){

            holder.outOfStock.show()
            holder.addToCart.hide()


        }else  {
            holder.addToCart.show()
            holder.outOfStock.hide()

            if (flag == "1" ){
                holder.addToCart.hide()
            }else {
                holder.addToCart.show()

            }


        }

        Log.d("MainImage",HelperUtils.BASE_URL + productItem.item_image)
        holder.productImage.load(HelperUtils.BASE_URL + productItem.item_image) {
            placeholder(R.drawable.image)
            error(R.drawable.image)
            scale(Scale.FIT)
            crossfade(true)
        }


        holder.favCheck.isChecked = productItem.favorite_status != 0

//        val price = if (productItem.on_sale == 1)
//            productItem.item_on_sale_price
//        else
        val price = productItem.item_price

        holder.productPrice.text = holder.itemView.context.getString(
            R.string.piece, price.toString()
        )

        val backgroundShapeModel: ShapeAppearanceModel = ShapeAppearanceModel.builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 12F)
            .setTopRightCorner(CornerFamily.ROUNDED, 12F)
            .setBottomLeftCorner(CornerFamily.ROUNDED, 12F)
            .setBottomRightCorner(CornerFamily.ROUNDED, 12F)
            .build()

        holder.addToCart.background = MaterialShapeDrawable(backgroundShapeModel).apply {
            fillColor = ColorStateList.valueOf(Color.rgb (60, 110, 62))
        }

    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}