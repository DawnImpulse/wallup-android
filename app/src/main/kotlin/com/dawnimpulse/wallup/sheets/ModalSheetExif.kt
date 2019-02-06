/*
ISC License

Copyright 2018, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/package com.dawnimpulse.wallup.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Config
import com.dawnimpulse.wallup.utils.L
import com.dawnimpulse.wallup.utils.toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.bottom_sheet_exif.*


/**
 * @author Saksham
 *
 * @note Last Branch Update -
 * @note Created on 2018-08-19 by Saksham
 *
 * @note Updates :
 */
class ModalSheetExif : RoundedBottomSheetDialogFragment() {
    private lateinit var details: ImagePojo
    private lateinit var model: UnsplashModel
    private var NAME = "ModalSheetExif"

    // on create
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_exif, container, false)
    }

    // on view created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        details = Gson().fromJson(arguments!!.getString(C.IMAGE_POJO), ImagePojo::class.java)
        model = UnsplashModel(lifecycle)
    }

    // on resume
    override fun onResume() {
        super.onResume()

        if (details.exif === null)
            model.getImage(details.id) { error, details ->
                if (error != null) {
                    L.d(NAME, error.toString())
                    context!!.toast("Error in Image Details")
                    dismiss()
                } else {
                    this.details = details as ImagePojo
                    Config.imagePojo = details
                    setImageExif()
                }
            }
        else
            setImageExif()
    }

    /**
     * set image exif
     */
    private fun setImageExif() {
        if (arguments!!.containsKey(C.COLOR))
            setColor(arguments!!.getInt(C.COLOR))
        sheetExifL.visibility = View.VISIBLE

        if (details.exif!!.make != null) {
            sheetExifMake.text = details.exif!!.make
        } else
            sheetExifMakeL.visibility = View.GONE


        if (details.exif!!.model != null) {
            sheetExifModel.text = details.exif!!.model
        } else
            sheetExifModelL.visibility = View.GONE

        if (details.exif!!.aperture != null) {
            sheetExifAperture.text = details.exif!!.aperture
        } else
            sheetExifApertureL.visibility = View.GONE

        if (details.exif!!.focal_length != null) {
            sheetExifFocal.text = details.exif!!.focal_length
        } else
            sheetExifFocalL.visibility = View.GONE

        if (details.exif!!.exposure_time != null) {
            sheetExifExposure.text = details.exif!!.exposure_time
        } else
            sheetExifExposureL.visibility = View.GONE

        if (details.exif!!.iso != null) {
            sheetExifIso.text = details.exif!!.iso.toString()
        } else
            sheetExifIsoL.visibility = View.GONE

        L.d(NAME, Gson().toJson(details.exif!!))
        if (Gson().toJson(details.exif!!).toString() == "{}")
            sheetExifNoData.visibility = View.VISIBLE
    }

    /**
     * set color on views
     */
    private fun setColor(color: Int) {
        sheetExifNoData.setTextColor(color)
        sheetExifApertureText.setTextColor(color)
        sheetExifMakeText.setTextColor(color)
        sheetExifModelText.setTextColor(color)
        sheetExifFocalText.setTextColor(color)
        sheetExifExposureText.setTextColor(color)
        sheetExifIsoText.setTextColor(color)
    }

}