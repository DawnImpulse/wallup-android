package com.dawnimpulse.wallup.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager


/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2018-11-28 by Saksham
 * @note Updates :
 */
class ConnectivityReceiver internal constructor(private val mConnectivityReceiverListener: ConnectivityReceiverListener) : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        mConnectivityReceiverListener.onNetworkConnectionChanged(isConnected(context))

    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {

        fun isConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnected
        }
    }
}