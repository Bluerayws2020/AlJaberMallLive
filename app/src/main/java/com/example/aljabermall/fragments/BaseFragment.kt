package com.example.aljabermall.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.example.aljabermall.R
import com.example.aljabermall.helpers.HelperUtils

abstract class BaseFragment<VB : ViewBinding?, VM : ViewModel> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!
    protected abstract val viewModel: VM
    protected var mContext: Context? = null
    private var progressDialog = ProgressDialogFragment()
    protected val userID by lazy { HelperUtils.getUID(context) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    protected fun showProgress() {
        if (!progressDialog.isAdded && !progressDialog.isResumed)
            progressDialog.show(childFragmentManager, "base_fragment")
    }

    protected fun hideProgress() {
        if (progressDialog.isAdded)
            progressDialog.dismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    fun toast(message: String) =
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()


    fun applicationContext(): Context = requireActivity().applicationContext

    fun isNetWorkAvailable(): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return if (networkCapabilities != null) {
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            }
        } else false
    }


    fun refreshFragment(context: Context){
        context.let {
            val fragmentManage = (context as? AppCompatActivity)?.supportFragmentManager
            fragmentManage.let {
                val curranFragment =fragmentManage?.findFragmentById(R.id.nav_host_fragment)
                curranFragment?.let {
                    val  fragmentTranaction = fragmentManage?.beginTransaction()
                    fragmentTranaction?.detach(it)
                    fragmentTranaction?.attach(it)
                    fragmentTranaction?.commit()

                }
            }
        }
    }




}