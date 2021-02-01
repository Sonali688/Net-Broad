package com.ecl.netbroad

import android.app.Dialog
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import com.ecl.netbroad.databinding.DialogNoInternetBinding

class MainActivity : AppCompatActivity(),NetworkStateReceiver.NetworkStateReceiverListener {

    private var dialog: Dialog? = null
    private var networkStateReceiver: NetworkStateReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setNetworkStateReceiver()


    }

    override fun onNetworkAvailable() {
        dialog?.dismiss()

    }

    override fun onNetworkUnavailable() {

        try {
            dialog = showNetWorkDialog()
        } catch (ex: Exception) {
            ex.message
        }
    }

    private fun setNetworkStateReceiver() {
        networkStateReceiver = NetworkStateReceiver(this)
        networkStateReceiver!!.addListener(this)

        val mIntent = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        applicationContext.registerReceiver(networkStateReceiver, mIntent)

    }
    private fun showNetWorkDialog(): Dialog? {
        dialog?.dismiss()

        try {
            val dialog = Dialog(this)
            this.dialog = dialog
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val binding = DialogNoInternetBinding.inflate(LayoutInflater.from(this))
            dialog.setContentView(binding.root)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            binding.btnSubmit.setOnClickListener {
                val mNetwork = AppUtils.checkInternet(this)

                if (mNetwork) {
                    dialog.dismiss()
                } else {
                   // AppUtils.setVibrate(this)

                    val msg = getString(R.string.disabled_internet)
                    AppUtils.showToast(this, msg)
                }
            }
        } catch (ex: Exception) {
            ex.message
        }

        return dialog
    }
}