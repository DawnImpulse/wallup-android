package com.dawnimpulse.wallup.utils.reusables

const val UNSPLASH_URL = "https://api.unsplash.com/"
const val GITHUB = "https://github.com/dawnimpulse"
const val INSTAGRAM = "https://instagram.com/dawnimpulse"
const val DRIBBBLE = "https://dribbble.com/dawnimpulse"
const val GOOGLE = "https://play.google.com/store/apps/details?id=com.dawnimpulse.wallup"
const val TWITTER = "https://twitter.com/dawnimpulse"
const val ICON_CREDITS = "https://wallup.sourcei.org/icons"
const val LIBRARY_CREDITS = "https://wallup.sourcei.org/library"
const val PRIVACY = "https://wallup.sourcei.org/privacy"
const val TNC = "https://wallup.sourcei.org/tnc"

const val AVAILABLE = "available"
const val LIMIT = "_limit"
const val START = "_start"
const val RANDOM = "random"
const val DEVICE = "device"
const val HOME = "home"
const val AUTHORIZATION = "Authorization"
const val COUNT ="count"
const val PER_PAGE = "per_page"
const val PAGE = "page"
const val ID = "id"
const val IMAGE = "image"
const val AUTH = "auth"
const val NIGHT_MODE_SYSTEM = "nightModeSystem"
const val NIGHT_MODE = "nightMode"
const val JPG = "jpg"
const val WEBP = "webp"
const val RELOAD_LIST = "reloadList"

const val TYPE_ERROR_LOADING = "ERROR LOADING"

object EVENT {
    object REMOVE {
        val BOOKMARK = "eventRemoveBookmark"
    }
    object ADD {
        val BOOKMARK = "eventAddBookmark"
    }
    object NOT {
        val BOOKMARKS = "eventNotBookmarks"
    }
}

object RELOAD {
    object MORE {
        val HOME = "reloadMoreFragmentHome"
        val RANDOM = "reloadMoreFragmentRandom"
        val L_DEVICES = "reloadMoreFragmentLatestDevice"
        val A_DEVICES = "reloadMoreFragmentAllDevice"
        val D_IMAGES = "reloadMoreFragmentDeviceImages"
        val BOOKMARKS = "reloadMoreBookmarks"
    }
}

object ERROR{
    object AUTH {
        object GOOGLE {
            val LOGIN = 1001
            val SIGNUP = 1002
            val SIGNUP_2 = 1003
            val FIREBASE = 1004
            val USER = 1005
            val USER_2 = 1006
        }
    }
    object LIST {
        val HOME = 1007
        val RANDOM = 1009
        val L_DEVICE = 1011
        val A_DEVICES = 1013
        val D_IMAGES = 1015
        val BOOKMARKS = 1017

        object MORE {
            val HOME = 1008
            val RANDOM = 1010
            val L_DEVICE = 1012
            val A_DEVICES = 1014
            val D_IMAGES = 1016
            val BOOKMARKS = 1018
        }
    }
}