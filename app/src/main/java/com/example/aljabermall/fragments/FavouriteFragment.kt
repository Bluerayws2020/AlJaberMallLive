package com.example.aljabermall.fragments

import android.content.Intent
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.blueray.aljabermall.PRODUCT_KEY
import com.blueray.aljabermall.ProductDetailActivity
import com.example.aljabermall.R
import com.example.aljabermall.adapters.HorizontalProductAdapter
import com.example.aljabermall.adapters.OnProductListener
import com.example.aljabermall.databinding.FragmentFavouriteBinding
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.models.ProductItems
import com.example.aljabermall.viewmodels.CartViewModel
import com.example.aljabermall.viewmodels.FavouriteViewModel


class FavouriteFragment : BaseFragment<FragmentFavouriteBinding, FavouriteViewModel>() {
    override val viewModel by viewModels<FavouriteViewModel>()
    private val cartViewModel by viewModels<CartViewModel>()
    private var productAdapter: HorizontalProductAdapter? = null
//    lateinit var FAV_ID: String

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavouriteBinding {
        return FragmentFavouriteBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view)

        if (!isNetWorkAvailable()) {
            binding.messageFavourite.show()
            binding.messageFavourite.text = getString(R.string.error)
        }

        setupFavouriteRecycler()
        HelperUtils.setDefaultLanguage(requireContext(),"ar")

        viewModel.getFavourite().observe(viewLifecycleOwner) { result ->
            binding.progressBarFavourite.hide()
            binding.swipeToRefresh.isRefreshing = false
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                        if (result.data.user_favorites.isNotEmpty()) {
                            productAdapter?.submitList(result.data.user_favorites)
//                            Toast.makeText(context, result.data.status.msg, Toast.LENGTH_LONG)
//                                .show()
                        }
                        else{
                            binding.messageFavourite.show()
//                            Toast.makeText(context, result.data.status.msg, Toast.LENGTH_LONG)
//                                .show()
                        }
                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    binding.messageFavourite.show()
                }
            }
        }

        viewModel.getAddToFavouriteMessage().observe(viewLifecycleOwner) { result ->
            hideProgress()
            when (result) {
                is NetworkResults.Success -> {
                    viewModel.retrieveFavourite()
                    toast(result.data.status.msg)
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    toast(getString(R.string.error))
                }
            }
        }

        viewModel.getRemoveFavouriteMessage().observe(viewLifecycleOwner) { result ->
            hideProgress()
            when (result) {
                is NetworkResults.Success -> {
//                    viewModel.retrieveFavourite()
    toast(result.data.status.msg)




                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    toast(getString(R.string.error))
                }
            }
        }

        cartViewModel.getAddToCartMessage().observe(viewLifecycleOwner) { result ->
            hideProgress()
            when (result) {
                is NetworkResults.Success -> {
                    toast(result.data.status.msg)
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    toast(getString(R.string.error))
                }
            }
        }

        viewModel.retrieveFavourite()

        binding.swipeToRefresh.setOnRefreshListener {
            binding.messageFavourite.hide()
            viewModel.retrieveFavourite()
        }
        binding.includeTab.backButton.setOnClickListener {
            navController.popBackStack()
        }
        binding.includeTab.tabTitle.text = getString(R.string.favourite)
    }

    private fun setupFavouriteRecycler() {
        swipeToDelete()
        productAdapter = HorizontalProductAdapter(object : OnProductListener {
//            NetworkRepository.addToCart(language, uid, productId, quantity, deviceId)


            override fun addToCart(price: Double?, pid: Int?, quantity: String) {
                showProgress()
                cartViewModel.addToCart(price?.toDouble()!!,pid.toString(), quantity)
            }

            override fun addToFavourite(pid: Int) {
                showProgress()
                viewModel.addToFavourite(pid.toString())
            }

            override fun removeFromFavourite(favId: Int) {
                showProgress()
                viewModel.removeFavourite(favId.toString())
            }

            override fun showDetails(product: ProductItems) {
                val intentProductDetail = Intent(mContext, ProductDetailActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable(PRODUCT_KEY, product)
                intentProductDetail.putExtras(bundle)
                startActivity(intentProductDetail)
            }

        })

        binding.favouriteRecycler.addItemDecoration(
            DividerItemDecoration(
                mContext,
                RecyclerView.VERTICAL
            )
        )

        binding.favouriteRecycler.adapter = productAdapter
    }


    private fun swipeToDelete()
    {
        val deleteIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_trash2000)
        val intrinsicWidth = deleteIcon?.intrinsicWidth
        val intrinsicHeight = deleteIcon?.intrinsicHeight
        val background = ColorDrawable()
        val backgroundColor = Color.parseColor("#f44336")

        val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT)
        {

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val swipeFlag = ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
                return makeMovementFlags(0, swipeFlag)

            }


            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }



            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top
                val isCanceled = dX == 0f && !isCurrentlyActive

                if (isCanceled) {
                    clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    return
                }

                // Draw the red delete background
                background.color = backgroundColor
                background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                background.draw(c)

                // Calculate position of delete icon
                val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
                val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
                val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth!!
                val deleteIconRight = itemView.right - deleteIconMargin
                val deleteIconBottom = deleteIconTop + intrinsicHeight

                // Draw the delete icon
                deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                deleteIcon.draw(c)

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
                c?.drawRect(left, top, right, bottom, clearPaint)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.removeFavourite(productAdapter!!.currentList[position].id.toString())

//                viewModel.retrieveFavourite()
                binding.favouriteRecycler.adapter?.notifyItemChanged(position)
            }

        }).attachToRecyclerView(binding.favouriteRecycler)
    }
}