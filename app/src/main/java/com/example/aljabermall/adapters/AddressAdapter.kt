package com.example.aljabermall.adapters

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aljabermall.databinding.ItemAddressBinding
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.models.LocationModel

class AddressAdapter(
    private val addressList: List<LocationModel>,
    private val onAddressListener: OnAddressListener,
    private val isAddressUpdate: Boolean,
    private val mContext: Context
) : RecyclerView.Adapter<AddressAdapter.AddressHolder>() {

    private var selectedLocationId = 0

    inner class AddressHolder(binding: ItemAddressBinding) : RecyclerView.ViewHolder(binding.root) {
        val addressName = binding.addressName
        private val deleteAddressBtn = binding.deleteAddress

        init {
            deleteAddressBtn.setOnClickListener {
                onAddressListener.deleteAddress(bindingAdapterPosition)
            }
            binding.root.setOnClickListener {
                if (isAddressUpdate)
                    onAddressListener.updateAddress(bindingAdapterPosition)
                else {
                    onAddressListener.selectAddress(bindingAdapterPosition)
                    selectedLocationId = addressList[bindingAdapterPosition].user_id
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressHolder {
        val binding = ItemAddressBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddressHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressHolder, position: Int) {
        val address = addressList[position]

        holder.addressName.text = address.fullAddress.address_line1


//        if(HelperUtils.getLang(mContext) == "ar"){
//            holder.addressName.gravity = Gravity.START
//            holder.addressName.text = address.fullAddress.address_line1
//        }


        if (address.user_id == selectedLocationId)
            holder.itemView.setBackgroundColor(Color.GRAY)
        else
            holder.itemView.setBackgroundColor(Color.WHITE)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    interface OnAddressListener {
        fun updateAddress(position: Int)
        fun deleteAddress(position: Int)
        fun selectAddress(position: Int)
    }
}