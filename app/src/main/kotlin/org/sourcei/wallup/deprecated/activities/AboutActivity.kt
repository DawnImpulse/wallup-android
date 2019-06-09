/*
ISC License

Copyright 2018-2019, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package org.sourcei.wallup.deprecated.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_about.*
import org.sourcei.wallup.deprecated.BuildConfig
import org.sourcei.wallup.deprecated.R
import org.sourcei.wallup.deprecated.utils.F
import org.sourcei.wallup.deprecated.utils.RemoteConfig
import org.sourcei.wallup.deprecated.utils.openActivity

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09 by Saksham
 *
 * @note Updates :
 */
class AboutActivity : AppCompatActivity(), View.OnClickListener {
    private val NAME = "AboutActivity"
    private var update: org.sourcei.wallup.deprecated.pojo.UpdatePojo? = null

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

        org.sourcei.wallup.deprecated.handlers.ImageHandler.setImageInView(lifecycle, aboutImage, RemoteConfig.getAboutImage())
        org.sourcei.wallup.deprecated.handlers.ImageHandler.setImageInView(lifecycle, aboutAuthorImage, org.sourcei.wallup.deprecated.utils.C.DI_IMAGE)

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
        aboutBack.setOnClickListener(this)
    }

    // on click
    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                aboutUpdate.id -> F.startWeb(this, org.sourcei.wallup.deprecated.utils.C.WALLUP_PLAY)
                changelog.id -> startActivity(Intent(this, org.sourcei.wallup.deprecated.activities.ChangesActivity::class.java))
                rate.id -> F.startWeb(this, org.sourcei.wallup.deprecated.utils.C.WALLUP_PLAY)
                source.id -> F.startWeb(this, org.sourcei.wallup.deprecated.utils.C.WALLUP_GITHUB)
                aboutGithub.id -> F.startWeb(this, org.sourcei.wallup.deprecated.utils.C.DI_GITHUB)
                aboutTwitter.id -> F.startWeb(this, org.sourcei.wallup.deprecated.utils.C.DI_TWITTER)
                aboutLinkedin.id -> F.startWeb(this, org.sourcei.wallup.deprecated.utils.C.DI_LINKEDIN)
                aboutUnsplash.id -> F.startWeb(this, org.sourcei.wallup.deprecated.utils.C.UNSPLASH)
                donate.id -> F.startWeb(this, org.sourcei.wallup.deprecated.utils.C.DI_PAYPAL)
                licenseLibs.id -> openActivity(org.sourcei.wallup.deprecated.activities.LibraryLicenseActivity::class.java)
                licenseIcons.id -> openActivity(org.sourcei.wallup.deprecated.activities.IconsActivity::class.java)
                privacy.id -> F.startWeb(this, RemoteConfig.getPrivacyLink())
                terms.id -> F.startWeb(this, RemoteConfig.getTnC())
                contact.id -> F.sendMail(this)
                aboutBack.id -> finish()
                else -> {
                }
            }
        }
    }
}
