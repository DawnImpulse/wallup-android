package com.dawnimpulse.wallup.receivers

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dawnimpulse.wallup.utils.Arrays
import com.dawnimpulse.wallup.utils.toast

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2018-12-17 by Saksham
 * @note Updates :
 */
class DownloadReceiver : BroadcastReceiver() {

    // on receive
    override fun onReceive(context: Context, intent: Intent) {
//check if the broadcast message is for our enqueued download
        val referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        if (Arrays.downloadIds.contains(referenceId)) {
            context.toast("Image downloaded successfully.")
        }
    }

}