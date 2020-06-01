package com.dawnimpulse.wallup.utils.handlers

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.dawnimpulse.wallup.utils.reusables.WEBP

class HandlerTransform {
    private lateinit var url: String
    private lateinit var view: ImageView
    private lateinit var context: Context
    private var height: Int? = null
    private var width: Int? = null
    private var format = WEBP
    private var quality: Int = 80
    private var blur: Int? = null

    constructor(url: String, view: ImageView) {
        this.url = url
        this.view = view
    }

    constructor(url: String, context: Context) {
        this.url = url
        this.context = context
    }

    /**
     * set height of image
     */
    fun height(height: Int): HandlerTransform {
        this.height = height
        return this
    }

    /**
     * set width of image
     */
    fun width(width: Int): HandlerTransform {
        this.width = width
        return this
    }

    /**
     * set format of image
     */
    fun format(format: String): HandlerTransform {
        this.format = format
        return this
    }

    /**
     * set quality of image
     */
    fun quality(quality: Int): HandlerTransform {
        this.quality = quality
        return this
    }

    /**
     * blur the image
     */
    fun blur(blur: Int): HandlerTransform {
        this.blur = blur;
        return this
    }

    /**
     * create url , fetch & set image
     */
    fun apply() {
        // check for pf param
        if (!url.contains("?"))
            url += "?pf=ik"

        // apply transform param
        height?.let { url += "&h=$it" }
        width?.let { url += "&w=$it" }
        blur?.let { url += "&bl=$it" }
        url += "&f=$format"
        url += "&q=$quality"

        // fetch and set image
        //HandlerImage.fetchAndSetImage(url, view)
    }

    /**
     * create url, fetch & return bitmap
     *
     * @param callback
     */
    fun bitmap(callback: (Bitmap?) -> Unit) {
        // check for pf param
        if (!url.contains("?"))
            url += "?pf=ik"

        // apply transform param
        height?.let { url += "&h=$it" }
        width?.let { url += "&w=$it" }
        url += "&f=$format"
        url += "&q=$quality"

        // fetch and set image
        HandlerImage.fetchImageBitmap(context, url) { callback(it) }
    }
}