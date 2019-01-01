package com.dawnimpulse.wallup.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.dawnimpulse.wallup.R

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2018-12-03 by Saksham
 * @note Updates :
 */
class Drawables(context: Context){
    val latest = ContextCompat.getDrawable(context, R.drawable.vd_latest_accent)
    val latest_outline = ContextCompat.getDrawable(context, R.drawable.vd_latest_outline)
    val shuffle = ContextCompat.getDrawable(context, R.drawable.vd_shuffle)
    val shuffle1 = ContextCompat.getDrawable(context, R.drawable.vd_shuffle_1)
    val shuffle2 = ContextCompat.getDrawable(context, R.drawable.vd_shuffle_2_accent)
    val shuffle_outline = ContextCompat.getDrawable(context, R.drawable.vd_shuffle_outline)
}