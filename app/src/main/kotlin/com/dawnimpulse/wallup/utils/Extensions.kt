package com.dawnimpulse.wallup.utils

/**
 * @info - custom kotlin extension functions
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2018-12-04 by Saksham
 * @note Updates :
 */

// int color to hexa string
fun Int.toHexa(): String {
    return String.format("#%06X", 0xFFFFFF and this)
}