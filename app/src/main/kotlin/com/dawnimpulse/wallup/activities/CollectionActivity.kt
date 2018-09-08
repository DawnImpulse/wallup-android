package com.dawnimpulse.wallup.activities

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.widget.TextView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.handlers.ColorHandler
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.pojo.CollectionPojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Config
import com.dawnimpulse.wallup.utils.F
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_collection.*

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-08 by Saksham
 *
 * @note Updates :
 */
class CollectionActivity : AppCompatActivity() {
    private lateinit var details: CollectionPojo
    private var color = 0

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)

        details = Gson().fromJson(intent.extras.getString(C.COLLECTION), CollectionPojo::class.java)
        setDetails()
    }

    // setting details in views
    private fun setDetails() {
        var name = details.user!!.name
        name = "<font color=\"#9e9e9e\">${F.firstWord(name)}</font> ${name.replace(F.firstWord(name), "")}"
        color = Color.parseColor(details.cover_photo.color!!)
        colUserName.setTextColor(color)
        colTitle.text = details.title
        colDescription.text = details.description
        colUserName.setText(F.fromHtml(name), TextView.BufferType.SPANNABLE)
        colImageC.setText(F.fromHtml("${details.total_photos} <font color=\"#9e9e9e\">photos</font>"), TextView.BufferType.SPANNABLE)
        colImageUpdated.setText(F.fromHtml("<font color=\"#9e9e9e\">updated on</font> ${F.dateConvert(details.updated_at)}"), TextView.BufferType.SPANNABLE)
        colPublished.setText(F.fromHtml("<font color=\"#9e9e9e\">published on</font> ${F.dateConvert(details.published_at)}"), TextView.BufferType.SPANNABLE)

        ImageHandler.getImageAsBitmap(lifecycle,this,details.cover_photo.urls?.full + Config.IMAGE_HEIGHT){
            val color = ColorHandler.getNonDarkColor(Palette.from(it).generate(),this)
            colImage.setImageBitmap(it)
            setColor(color)
        }
        ImageHandler.setImageInView(lifecycle, colUserImage, details.user?.profile_image!!.large)

        setColor(color)
    }

    // set color on views
    private fun setColor(color:Int){
        colTitle.setTextColor(color)
        colImageC.setTextColor(color)
        //colDescription.setTextColor(color)
        colUserName.setTextColor(color)
        colImageUpdated.setTextColor(color)
        colPublished.setTextColor(color)
    }
}
