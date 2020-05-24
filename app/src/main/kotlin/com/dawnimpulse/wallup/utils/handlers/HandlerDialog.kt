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
        with(dialog) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setCancelable(false)
            setContentView(R.layout.dialog_loading)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            setOnDismissListener {
                dialog_loading_animation.pauseAnimation()
            }
            show()
        }
    }

    /**
     * loading dialog (cancellable)
     */
    fun loading(activity: Activity, dismiss: () -> Unit) {
        dialog = Dialog(activity)
        with(dialog) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setCancelable(true)
            setContentView(R.layout.dialog_loading)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            setOnDismissListener {
                dialog_loading_animation.pauseAnimation()
                dismiss()
            }
            show()
        }
    }

    /**
     * dismiss dialog
     */
    fun dismiss(){
        dialog.dismiss()
    }
}