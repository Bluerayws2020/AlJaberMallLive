package com.example.aljabermall.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.aljabermall.R
import com.example.aljabermall.adapters.GenericAdapter
import com.example.aljabermall.databinding.DialogBranchesBinding
import com.example.aljabermall.databinding.ItemBranchBinding
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.BranchItem
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.viewmodels.BranchViewModel

const val CANCELABLE = "CANCELABLE"
const val BRANCH_DIALOG = "BRANCH_DIALOG"
const val BRANCH_ID = "BRANCH"

class BranchesDialog : DialogFragment() {

    private val branchViewModel by viewModels<BranchViewModel>()
    private var bindings: DialogBranchesBinding? = null
    private var mContext: Context? = null
    private var language = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindings = DialogBranchesBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        isCancelable = HelperUtils.isBranchSelected(mContext)
        return bindings?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isCancelEnabled = arguments?.getBoolean(CANCELABLE)

        branchViewModel.getBranches().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 1)
                        setupBranchesRecycler(result.data.branches)
                    bindings?.progressBranches?.hide()
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }

        if (isCancelEnabled == true) {
            bindings?.cancelBranchBtn?.show()
            bindings?.cancelBranchBtn?.setOnClickListener {
                dialog?.dismiss()
            }
        } else
            bindings?.cancelBranchBtn?.hide()
    }

    private fun setupBranchesRecycler(branchesList: List<BranchItem>) {
        val branchesAdapter = object : GenericAdapter<BranchItem, ItemBranchBinding>(branchesList) {
            override fun onBindData(model: BranchItem?, dataBinding: ItemBranchBinding?) {
                if (language == "ar")
                    dataBinding?.branchTitle?.text = model?.name_ar
                else
                    dataBinding?.branchTitle?.text = model?.name_en
            }

            override fun onItemClick(model: BranchItem?) {
                val branchId = model?.id!!
                if (branchId != 0) {
                    val bundle = Bundle()
                    bundle.putInt(BRANCH_ID, branchId)
                    setFragmentResult(BRANCH_DIALOG, bundle)
                    dismiss()
                } else
                    Toast.makeText(
                        mContext,
                        getString(R.string.choose_branch_warning),
                        Toast.LENGTH_LONG
                    ).show()
            }

            override fun getViewBinding(viewGroup: ViewGroup?): ItemBranchBinding {
                return ItemBranchBinding.inflate(
                    LayoutInflater.from(viewGroup?.context),
                    viewGroup,
                    false
                )
            }

        }

        bindings?.branchesRecycler?.addItemDecoration(
            DividerItemDecoration(
                mContext,
                RecyclerView.VERTICAL
            )
        )
        bindings?.branchesRecycler?.adapter = branchesAdapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDestroyView() {
        bindings = null
        super.onDestroyView()
    }
}