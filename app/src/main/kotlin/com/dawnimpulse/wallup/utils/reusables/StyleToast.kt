package com.dawnimpulse.wallup.utils.reusables

import android.widget.Toast
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.App
import com.muddzdev.styleabletoast.StyleableToast

object StyleToast {
    /**
     * info
     * @param text
     * @param length
     */
    fun info(text: Any, length: Int = Toast.LENGTH_SHORT) {
        return StyleableToast
                .Builder(App.context)
                .textColor(Colors.WHITE)
                .backgroundColor(Colors.GREY_TOAST)
                .solidBackground()
                .iconStart(R.drawable.vd_info_white)
                .text(text.toString())
                .length(length)
                .show()
    }

    /**
     * error
     * @param text
     * @param length
     */
    fun error(text: Any, length: Int = Toast.LENGTH_LONG) {
        return StyleableToast
                .Builder(App.context)
                .textColor(Colors.WHITE)
                .backgroundColor(Colors.RED)
                .solidBackground()
                .iconStart(R.drawable.vd_close_white)
                .text(text.toString())
                .length(length)
                .show()
    }

    /**
     * success
     * @param length
     */
    fun success(text: Any, length: Int = Toast.LENGTH_SHORT) {
        return StyleableToast
                .Builder(App.context)
                .textColor(Colors.WHITE)
                .backgroundColor(Colors.GREEN)
                .solidBackground()
                .iconStart(R.drawable.vd_check_white)
                .text(text.toString())
                .length(length)
                .show()
    }

}