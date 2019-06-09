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
package org.sourcei.wallup.deprecated.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.bottom_sheet_exif.*
import org.sourcei.wallup.deprecated.R
import org.sourcei.wallup.deprecated.utils.L
import org.sourcei.wallup.deprecated.utils.toast


/**
 * @author Saksham
 *
 * @note Last Branch Update -
 * @note Created on 2018-08-19 by Saksham
 *
 * @note Updates :
 */
class ModalSheetExif : org.sourcei.wallup.deprecated.sheets.RoundedBottomSheetDialogFragment() {
    private lateinit var details: org.sourcei.wallup.deprecated.pojo.ImagePojo
    private lateinit var model: org.sourcei.wallup.deprecated.models.UnsplashModel
    private var NAME = "ModalSheetExif"

    // on create
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_exif, container, false)
    }

    // on view created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        details = Gson().fromJson(arguments!!.getString(org.sourcei.wallup.deprecated.utils.C.IMAGE_POJO), org.sourcei.wallup.deprecated.pojo.ImagePojo::class.java)
        model = org.sourcei.wallup.deprecated.models.UnsplashModel(lifecycle)
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
                    this.details = details as org.sourcei.wallup.deprecated.pojo.ImagePojo
                    org.sourcei.wallup.deprecated.utils.Config.imagePojo = details
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
        if (arguments!!.containsKey(org.sourcei.wallup.deprecated.utils.C.COLOR))
            setColor(arguments!!.getInt(org.sourcei.wallup.deprecated.utils.C.COLOR))
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