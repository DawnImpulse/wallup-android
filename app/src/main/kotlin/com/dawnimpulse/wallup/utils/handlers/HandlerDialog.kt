package com.dawnimpulse.wallup.utils.handlers

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import com.dawnimpulse.wallup.R
import kotlinx.android.synthetic.main.dialog_loading.*

object HandlerDialog {
    private lateinit var dialog: Dialog

    /**
     * loading dialog (non cancellable)
     */
    fun loading(activity: Activity) {
        dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setOnDismissListener {
            dialog.dialog_loading_animation.pauseAnimation()
        }
        dialog.show()
    }

    /**
     * loading dialog (cancellable)
     */
    fun loading(activity: Activity, dismiss: () -> Unit) {
        dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setOnDismissListener {
            dialog.dialog_loading_animation.pauseAnimation()
            dismiss()
        }
        dialog.show()
    }

    /**
     * dismiss dialog
     */
    fun dismiss(){
        dialog.dismiss()
    }
}