package com.dawnimpulse.wallup.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import androidx.core.widget.toast
import com.dawnimpulse.wallup.BuildConfig
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.pojo.UpdatePojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.F
import com.dawnimpulse.wallup.utils.RemoteConfig
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity(), View.OnClickListener {
    private val NAME = "AboutActivity"
    private var update: UpdatePojo? = null

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        aboutVersion.text = "v${BuildConfig.VERSION_NAME}"

        update = RemoteConfig.getProductionUpdateValues()
        update?.let {
            aboutLastUpdate.text = it.last_update_date

            if (it.next_version_code > BuildConfig.VERSION_CODE)
                aboutUpdate.visibility = View.VISIBLE
            else if (it.next_update_date.isNotEmpty()) {
                aboutNextUpdateL.visibility = View.VISIBLE
                aboutNextUpdate.text = it.next_update_date
            }
        }

        ImageHandler.setImageInView(lifecycle, aboutImage, RemoteConfig.getAboutImage())
        ImageHandler.setImageInView(lifecycle, aboutAuthorImage, C.DI_IMAGE)

        aboutUpdate.setOnClickListener(this)
        changelog.setOnClickListener(this)
        rate.setOnClickListener(this)
        source.setOnClickListener(this)
        //bugFeature.setOnClickListener(this)
        aboutGithub.setOnClickListener(this)
        aboutTwitter.setOnClickListener(this)
        aboutLinkedin.setOnClickListener(this)
        aboutUnsplash.setOnClickListener(this)
        donate.setOnClickListener(this)
        licenseLibs.setOnClickListener(this)
        licenseIcons.setOnClickListener(this)
        privacy.setOnClickListener(this)
        terms.setOnClickListener(this)
        contact.setOnClickListener(this)
    }

    // on click
    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                aboutUpdate.id -> F.startWeb(this, C.WALLUP_PLAY)
                changelog.id -> startActivity(Intent(this, ChangesActivity::class.java))
                rate.id -> F.startWeb(this, C.WALLUP_PLAY)
                source.id -> F.startWeb(this, C.WALLUP_GITHUB)
                aboutGithub.id -> F.startWeb(this, C.DI_GITHUB)
                aboutTwitter.id -> F.startWeb(this, C.DI_TWITTER)
                aboutLinkedin.id -> F.startWeb(this, C.DI_LINKEDIN)
                aboutUnsplash.id -> F.startWeb(this, C.UNSPLASH)
                donate.id -> F.startWeb(this, C.DI_PAYPAL)
                licenseLibs.id -> toast("upcoming")
                licenseIcons.id -> toast("upcoming")
                privacy.id -> F.startWeb(this, RemoteConfig.getPrivacyLink())
                terms.id -> F.startWeb(this, RemoteConfig.getTnC())
                contact.id -> F.sendMail(this)
                else -> {
                }
            }
        }
    }
}
