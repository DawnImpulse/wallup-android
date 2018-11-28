package com.dawnimpulse.wallup.utils

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log


/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2018-11-28 by Saksham
 * @note Updates :
 */
class NetworkSchedulerService : JobService(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var mConnectivityReceiver: ConnectivityReceiver? = null

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Service created")
        mConnectivityReceiver = ConnectivityReceiver(this)
    }


    /**
     * When the app's NetworkConnectionActivity is created, it starts this service. This is so that the
     * activity and this service can communicate back and forth. See "setUiCallback()"
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand")
        return START_NOT_STICKY
    }


    override fun onStartJob(params: JobParameters): Boolean {
        Log.i(TAG, "onStartJob" + mConnectivityReceiver!!)
        registerReceiver(mConnectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        Log.i(TAG, "onStopJob")
        unregisterReceiver(mConnectivityReceiver)
        return true
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        val message = if (isConnected) "Good! Connected to Internet" else "Sorry! Not connected to internet"
        /*Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()*/

    }

    companion object {

        private val TAG = NetworkSchedulerService::class.java.simpleName
    }
}