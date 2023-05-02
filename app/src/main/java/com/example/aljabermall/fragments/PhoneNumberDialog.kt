package com.example.aljabermall.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.aljabermall.R
import com.example.aljabermall.databinding.DialogPhoneNumberBinding
import com.example.aljabermall.helpers.ViewUtils.isInputEmpty

class PhoneNumberDialog(
    private val message: String,
    private val onPhoneSubmitListener: (String) -> Unit,
) : DialogFragment() {

    private var binding: DialogPhoneNumberBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPhoneNumberBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.dialogTitle?.text = message

        binding?.changePhoneBtn?.setOnClickListener {
            if (binding?.phoneNumberEt?.isInputEmpty() == true) {
                binding?.phoneNumberEt?.error = getString(R.string.error)
            } else {
                onPhoneSubmitListener(binding?.phoneNumberEt?.text.toString())
                dismiss()
            }
        }
        binding?.cancelDialogBtn?.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}