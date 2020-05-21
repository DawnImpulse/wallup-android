package com.dawnimpulse.wallup.ui.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dawnimpulse.wallup.BuildConfig
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.utils.reusables.*
import kotlinx.android.synthetic.main.activity_info.*

class ActivityInfo : AppCompatActivity(R.layout.activity_info), View.OnClickListener {

    /**
     * on create
     */
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity_info_version.text = "v${BuildConfig.VERSION_NAME}"
        activity_info_github.setOnClickListener(this)
        activity_info_insta.setOnClickListener(this)
        activity_info_dribbble.setOnClickListener(this)
        activity_info_google.setOnClickListener(this)
        activity_info_email.setOnClickListener(this)
        activity_info_twitter.setOnClickListener(this)
        activity_info_icon_credits.setOnClickListener(this)
        activity_info_library_credits.setOnClickListener(this)
        activity_info_privacy.setOnClickListener(this)
        activity_info_tnc.setOnClickListener(this)
    }

    /**
     * on resume
     */
    override fun onResume() {
        super.onResume()

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            window.decorView.systemUiVisibility = 0
        else
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

    }

    /**
     * on click handling
     */
    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                activity_info_github.id -> startWeb(GITHUB)
                activity_info_insta.id -> startWeb(INSTAGRAM)
                activity_info_dribbble.id -> startWeb(DRIBBBLE)
                activity_info_google.id -> startWeb(GOOGLE)
                activity_info_email.id -> F.sendMail(this)
                activity_info_twitter.id -> startWeb(TWITTER)
                activity_info_icon_credits.id -> startWeb(ICON_CREDITS)
                activity_info_library_credits.id -> startWeb(LIBRARY_CREDITS)
                activity_info_privacy.id -> startWeb(PRIVACY)
                activity_info_tnc.id -> startWeb(TNC)
            }
        }
    }

}