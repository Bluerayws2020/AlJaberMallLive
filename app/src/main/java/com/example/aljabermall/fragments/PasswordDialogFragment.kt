package com.example.aljabermall.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.aljabermall.R
import com.example.aljabermall.databinding.DialogPasswordBinding
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.viewmodels.UserViewModel

class PasswordDialogFragment : DialogFragment() {

    private var binding: DialogPasswordBinding? = null
    private val userVM by viewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPasswordBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userVM.getResetPassResponse().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    showMessage(result.data.msg)
                    if (result.data.status == 1) {
                        dismiss()
                    } else {
                        binding?.progressPassword?.hide()
                        binding?.actionLayout?.show()
                    }
                }
                is NetworkResults.Error -> {
                    binding?.progressPassword?.hide()
                    binding?.actionLayout?.show()
                    showMessage(getString(R.string.error))
                    result.exception.printStackTrace()
                }
            }
        }

        binding?.resetPassBtn?.setOnClickListener {
            val email = binding?.emailEt?.text.toString()
            if (email.isEmpty()) {
                binding?.emailEt?.error = getString(R.string.required)
            } else {
                binding?.actionLayout?.hide()
                binding?.progressPassword?.show()
                userVM.resetPassword(email)
            }
        }

        binding?.cancelDialogBtn?.setOnClickListener {
            dismiss()
        }
    }

    private fun showMessage(message: String?) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}