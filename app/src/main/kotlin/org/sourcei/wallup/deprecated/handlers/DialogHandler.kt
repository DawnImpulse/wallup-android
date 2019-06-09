/*
ISC License

Copyright 2018-2019, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/package org.sourcei.wallup.deprecated.handlers

import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.dialog_download.view.*
import kotlinx.android.synthetic.main.dialog_progress.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.grantland.widget.AutofitTextView
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import org.sourcei.wallup.deprecated.R
import org.sourcei.wallup.deprecated.utils.*


/**
 * @author Saksham
 *
 * @note Last Branch Update -
 * @note Created on 2018-10-04 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 12 16 - master - download dialog
 *  Saksham - 2018 12 16 - master - progress dialog
 *  Saksham - 2018 12 20 - master - callback for download ok
 */
object DialogHandler {
    private val NAME = "DialogHandler"
    private lateinit var alertDialog: AlertDialog

    // simple ok dialog
    fun simpleOk(context: Context, title: String, message: String, positive: DialogInterface.OnClickListener) {
        var builder = AlertDialog.Builder(context, R.style.MyDialogTheme)
        builder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", positive)
                .setNegativeButton("CANCEL") { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(false)

        org.sourcei.wallup.deprecated.handlers.DialogHandler.alertDialog = builder.create()
        org.sourcei.wallup.deprecated.handlers.DialogHandler.alertDialog.show()

    }

    // new collection dialog
    // @param image - make it null if only collection to be added
    fun newCollection(context: Context, lifecycle: Lifecycle, image: String?) {
        val factory = LayoutInflater.from(context)
        val view = factory.inflate(R.layout.dialog_new_collection, null)
        org.sourcei.wallup.deprecated.handlers.DialogHandler.alertDialog = AlertDialog.Builder(context, R.style.MyDialogTheme).create()
        org.sourcei.wallup.deprecated.handlers.DialogHandler.alertDialog.setView(view)

        val layout = view.findViewById<View>(R.id.newColLayout) as LinearLayout
        val layoutP = view.findViewById<View>(R.id.newColPL) as LinearLayout
        val title = view.findViewById<View>(R.id.newColTitle) as TextInputEditText
        val titleL = view.findViewById<View>(R.id.newColTitleL) as TextInputLayout
        val desc = view.findViewById<View>(R.id.newColDescription) as TextInputEditText
        val switch = view.findViewById<View>(R.id.newColSwitch) as SwitchCompat
        val ok = view.findViewById<View>(R.id.newColOk) as Button
        val cancel = view.findViewById<View>(R.id.newColCancel) as Button

        val pcd = view.findViewById<View>(R.id.newColCreateColDone)
        val pcpl = view.findViewById<View>(R.id.newColCreateColPL)
        val pcpi = view.findViewById<View>(R.id.newColCreateColPI)
        val pcr = view.findViewById<View>(R.id.newColCreateColR)
        val pct = view.findViewById<View>(R.id.newColCreateColT) as TextView

        val pal = view.findViewById<View>(R.id.newColAddL)
        val pad = view.findViewById<View>(R.id.newColAddDone)
        val papl = view.findViewById<View>(R.id.newColAddPL)
        val papi = view.findViewById<View>(R.id.newColAddPI)
        val par = view.findViewById<View>(R.id.newColAddR)
        val pat = view.findViewById<View>(R.id.newColAddT) as TextView

        var obj = org.sourcei.wallup.deprecated.pojo.NewCollections("")
        var colPojo: Any = ""
        val model = org.sourcei.wallup.deprecated.models.UnsplashModel(lifecycle)

        // add image to new collection
        fun addImage(pojo: org.sourcei.wallup.deprecated.pojo.CollectionPojo, image: String) {
            model.addImageInCollection(image, pojo.id) { e, r ->
                e?.let {
                    L.d(org.sourcei.wallup.deprecated.handlers.DialogHandler.NAME, e)
                    context.toast("error adding image")
                    papl.gone()
                    par.show()
                }
                r?.let {
                    pat.text = "Image Added"
                    papl.gone()
                    pad.show()

                    var json = JSONObject()
                    json.put(org.sourcei.wallup.deprecated.utils.C.TYPE, org.sourcei.wallup.deprecated.utils.C.IMAGE_TO_COLLECTION)
                    json.put(org.sourcei.wallup.deprecated.utils.C.IS_ADDED, true)
                    json.put(org.sourcei.wallup.deprecated.utils.C.COLLECTION_ID, pojo.id)
                    json.put(org.sourcei.wallup.deprecated.utils.C.POSITION, 0)
                    json.put(org.sourcei.wallup.deprecated.utils.C.COLLECTION, F.toJson(pojo))
                    json.put(org.sourcei.wallup.deprecated.utils.C.IMAGE, image)
                    EventBus.getDefault().post(org.sourcei.wallup.deprecated.utils.Event(json))

                    GlobalScope.launch {
                        delay(500)
                        org.sourcei.wallup.deprecated.handlers.DialogHandler.dismiss()
                    }
                }
            }
        }

        // create new collection
        fun createCollection(image: String?) {
            model.newCollection(obj) { e, r ->
                e?.let {
                    L.d(org.sourcei.wallup.deprecated.handlers.DialogHandler.NAME, e)
                    context.toast("error creating collection")
                    pcpl.gone()
                    pal.gone()
                    pcr.show()
                }
                r?.let {
                    r as org.sourcei.wallup.deprecated.pojo.CollectionPojo
                    colPojo = r
                    pcpl.gone()
                    pcd.show()
                    pct.text = "Collection Created"

                    var obj = JSONObject()
                    obj.put(org.sourcei.wallup.deprecated.utils.C.TYPE, org.sourcei.wallup.deprecated.utils.C.NEW_COLLECTION)
                    obj.put(org.sourcei.wallup.deprecated.utils.C.COLLECTION, F.toJson(r))
                    EventBus.getDefault().post(org.sourcei.wallup.deprecated.utils.Event(obj))

                    if (image == null) {
                        GlobalScope.launch {
                            delay(500)
                            org.sourcei.wallup.deprecated.handlers.DialogHandler.dismiss()
                        }
                    } else
                        addImage(r, image)
                }
            }
        }

        ok.setOnClickListener {
            if (title.text.toString().isNotEmpty()) {
                val title = title.text.toString()
                var desc: String? = desc.text.toString()
                val private = switch.isChecked

                desc = if (desc!!.isEmpty()) null else desc
                obj = org.sourcei.wallup.deprecated.pojo.NewCollections(title, desc, private)

                layout.hide()
                layoutP.show()
                if (image == null)
                    pal.gone()

                pcpi.animation = AnimationUtils.loadAnimation(context, R.anim.rotation_progress)
                papi.animation = AnimationUtils.loadAnimation(context, R.anim.rotation_progress)

                createCollection(image)
            } else
                titleL.error = "* Mandatory Field"
        }
        cancel.setOnClickListener {
            org.sourcei.wallup.deprecated.handlers.DialogHandler.dismiss()
        }
        pcr.setOnClickListener {
            pcpl.show()
            pcr.gone()
            image?.let {
                pal.show()
            }
            createCollection(image)
        }
        par.setOnClickListener {
            pal.show()
            par.gone()
            addImage(colPojo as org.sourcei.wallup.deprecated.pojo.CollectionPojo, image!!)
        }


        org.sourcei.wallup.deprecated.handlers.DialogHandler.alertDialog.show()
    }

    //download dialog
    fun download(context: Context, id: String, url: String, isWallpaper: Boolean = false, callback: () -> Unit) {
        val factory = LayoutInflater.from(context)
        val view = factory.inflate(R.layout.dialog_download, null)
        var shouldShow: Boolean = Prefs.getBoolean(org.sourcei.wallup.deprecated.utils.C.IMAGE_DOWNLOAD_ASK, true)
        org.sourcei.wallup.deprecated.handlers.DialogHandler.alertDialog = AlertDialog.Builder(context, R.style.MyDialogTheme).create()
        org.sourcei.wallup.deprecated.handlers.DialogHandler.alertDialog.setView(view)

        val path = view.downloadPath as AutofitTextView
        val or = view.downloadOT as TextView
        val orL = view.downloadO as LinearLayout
        val uhd = view.downloadUHDT as TextView
        val uhdL = view.downloadUHD as LinearLayout
        val fhd = view.downloadFHDT as TextView
        val fhdL = view.downloadFHD as LinearLayout
        val ask = view.dialogAsk as CheckBox
        val askL = view.dialogAskL as RelativeLayout
        val dT = view.downloadText as TextView
        val notice = view.downloadNotice


        ask.setOnCheckedChangeListener { _, isChecked ->
            /*if (isWallpaper)
                Prefs.putBoolean(C.IMAGE_WALLPAPER_ASK, isChecked)
            else*/
            Prefs.putBoolean(org.sourcei.wallup.deprecated.utils.C.IMAGE_DOWNLOAD_ASK, isChecked)
        }

        val drawable = ContextCompat.getDrawable(context, R.drawable.bt_round_complete_corners)
        val white = org.sourcei.wallup.deprecated.utils.Colors(context).WHITE
        val black = org.sourcei.wallup.deprecated.utils.Colors(context).BLACK
        val buttonC = null

        val clickListener = View.OnClickListener {
            when (it.id) {
                view.downloadChoose.id -> {
                    context.openActivity(org.sourcei.wallup.deprecated.handlers.DialogHandler.FolderPicker::class.java)
                    org.sourcei.wallup.deprecated.handlers.DialogHandler.dismiss()
                }
                orL.id -> {
                    Prefs.putString(org.sourcei.wallup.deprecated.utils.C.IMAGE_DOWNLOAD_QUALITY, org.sourcei.wallup.deprecated.utils.C.O)
                    org.sourcei.wallup.deprecated.utils.Config.IMAGE_DOWNLOAD_QUALITY = org.sourcei.wallup.deprecated.utils.C.O

                    or.background = drawable
                    or.setTextColor(black)

                    uhd.background = buttonC
                    uhd.setTextColor(white)

                    fhd.background = buttonC
                    fhd.setTextColor(white)
                }
                uhdL.id -> {
                    /*if (isWallpaper)
                        Prefs.putString(C.IMAGE_WALLPAPER_QUALITY, C.UHD)
                    else*/
                    Prefs.putString(org.sourcei.wallup.deprecated.utils.C.IMAGE_DOWNLOAD_QUALITY, org.sourcei.wallup.deprecated.utils.C.UHD)
                    org.sourcei.wallup.deprecated.utils.Config.IMAGE_DOWNLOAD_QUALITY = org.sourcei.wallup.deprecated.utils.C.UHD

                    or.background = buttonC
                    or.setTextColor(white)

                    uhd.background = drawable
                    uhd.setTextColor(black)

                    fhd.background = buttonC
                    fhd.setTextColor(white)
                }
                fhdL.id -> {
                    /*if (isWallpaper)
                        Prefs.putString(C.IMAGE_WALLPAPER_QUALITY, C.FHD)
                    else*/
                    Prefs.putString(org.sourcei.wallup.deprecated.utils.C.IMAGE_DOWNLOAD_QUALITY, org.sourcei.wallup.deprecated.utils.C.FHD)
                    org.sourcei.wallup.deprecated.utils.Config.IMAGE_DOWNLOAD_QUALITY = org.sourcei.wallup.deprecated.utils.C.FHD

                    or.background = buttonC
                    or.setTextColor(white)

                    uhd.background = buttonC
                    uhd.setTextColor(white)

                    fhd.background = drawable
                    fhd.setTextColor(black)
                }
                askL.id -> {
                    val value = !ask.isChecked
                    /*if (isWallpaper)
                        Prefs.putBoolean(C.IMAGE_WALLPAPER_ASK, value)
                    else*/
                    Prefs.putBoolean(org.sourcei.wallup.deprecated.utils.C.IMAGE_DOWNLOAD_ASK, value)
                    ask.isChecked = value
                    context.toast(Prefs.getBoolean(org.sourcei.wallup.deprecated.utils.C.IMAGE_DOWNLOAD_ASK, true).toString())
                }
            }
        }

        val pathString = Prefs.getString(org.sourcei.wallup.deprecated.utils.C.DOWNLOAD_PATH, org.sourcei.wallup.deprecated.utils.Config.DEFAULT_DOWNLOAD_PATH)
        path.text = pathString.toFileString()

        if (pathString.contains(":") && !pathString.contains("primary"))
            notice.show()

        orL.setOnClickListener(clickListener)
        uhdL.setOnClickListener(clickListener)
        fhdL.setOnClickListener(clickListener)
        askL.setOnClickListener(clickListener)
        view.downloadChoose.setOnClickListener(clickListener)

        /*if (isWallpaper) {
            when (Prefs.getString(C.IMAGE_WALLPAPER_QUALITY, C.FHD)) {
                C.FHD -> fhdL.performClick()
                C.UHD -> uhdL.performClick()
            }
        } else {*/
        when (Prefs.getString(org.sourcei.wallup.deprecated.utils.C.IMAGE_DOWNLOAD_QUALITY, org.sourcei.wallup.deprecated.utils.C.O)) {
            org.sourcei.wallup.deprecated.utils.C.FHD -> fhdL.performClick()
            org.sourcei.wallup.deprecated.utils.C.UHD -> uhdL.performClick()
            org.sourcei.wallup.deprecated.utils.C.O -> orL.performClick()
        }


        fun startDownloadOrWallpaper() {
            if (isWallpaper) {
                if (path.text.toString().toFile().exists()) {
                    org.sourcei.wallup.deprecated.handlers.DialogHandler.dismiss()
                    EventBus.getDefault().post(org.sourcei.wallup.deprecated.utils.Event(jsonOf(Pair(org.sourcei.wallup.deprecated.utils.C.TYPE, org.sourcei.wallup.deprecated.utils.C.WALLPAPER))))
                    callback()
                } else {
                    context.toast("File path doesn't exists , please select a new one.", Toast.LENGTH_LONG)
                    Prefs.putBoolean(org.sourcei.wallup.deprecated.utils.C.IMAGE_DOWNLOAD_ASK, true)
                }
            } else {
                if (path.text.toString().toFile().exists()) {
                    context.toast("Download starting!! Check notification for progress.")
                    val quality = Prefs.getString(org.sourcei.wallup.deprecated.utils.C.IMAGE_DOWNLOAD_QUALITY, org.sourcei.wallup.deprecated.utils.Config.IMAGE_DOWNLOAD_QUALITY)
                    var newUrl = url
                    if (quality != org.sourcei.wallup.deprecated.utils.C.O)
                        newUrl = "$newUrl&q=$quality"

                    org.sourcei.wallup.deprecated.handlers.DownloadHandler.downloadData(context, newUrl, "${id}_${quality.replace("&h=", "")}",
                            Prefs.getString(org.sourcei.wallup.deprecated.utils.C.DOWNLOAD_PATH, org.sourcei.wallup.deprecated.utils.Config.DEFAULT_DOWNLOAD_PATH).toFileString(), isWallpaper)
                    callback()
                } else {
                    context.toast("File path doesn't exists , please select a new one.", Toast.LENGTH_LONG)
                    Prefs.putBoolean(org.sourcei.wallup.deprecated.utils.C.IMAGE_DOWNLOAD_ASK, true)
                }
            }
        }

        org.sourcei.wallup.deprecated.handlers.DialogHandler.alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
            startDownloadOrWallpaper()
        }
        org.sourcei.wallup.deprecated.handlers.DialogHandler.alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL") { _, _ ->
        }

        org.sourcei.wallup.deprecated.handlers.DialogHandler.alertDialog.setCancelable(false)
        if (shouldShow) {
            org.sourcei.wallup.deprecated.handlers.DialogHandler.alertDialog.show()
        } else {
            if (path.text.toString().toFile().exists())
                startDownloadOrWallpaper()
            else {
                context.toast("File path doesn't exists , please select a new one.", Toast.LENGTH_LONG)
                Prefs.putBoolean(org.sourcei.wallup.deprecated.utils.C.IMAGE_DOWNLOAD_ASK, true)
                org.sourcei.wallup.deprecated.handlers.DialogHandler.alertDialog.show()
            }
        }

    }

    //progress dialog
    fun downloadProgress(context: Context, path: String, url: String, id: String, callback: (Boolean) -> Unit) {
        var did: Long? = null
        val factory = LayoutInflater.from(context)
        val view = factory.inflate(R.layout.dialog_progress, null)
        org.sourcei.wallup.deprecated.handlers.DialogHandler.alertDialog = AlertDialog.Builder(context, R.style.MyDialogTheme).create()
        org.sourcei.wallup.deprecated.handlers.DialogHandler.alertDialog.setView(view)

        val progress = view.progressSize
        val percentage = view.progressPercentage
        val bar = view.progressBar as ProgressBar

        org.sourcei.wallup.deprecated.handlers.DialogHandler.alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "CANCEL") { _, _ ->
            did?.let {
                val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                manager.remove(it)
            }
            EventBus.getDefault().post(org.sourcei.wallup.deprecated.utils.Event(jsonOf(Pair(org.sourcei.wallup.deprecated.utils.C.TYPE, org.sourcei.wallup.deprecated.utils.C.CANCEL))))
            org.sourcei.wallup.deprecated.handlers.DialogHandler.dismiss()
        }

        bar.isIndeterminate = true
        org.sourcei.wallup.deprecated.handlers.DownloadHandler.downloadManager(context, url, path, "$id.jpg", {
            bar.isIndeterminate = false
            L.d(org.sourcei.wallup.deprecated.handlers.DialogHandler.NAME, "${it.first} :: ${it.second}")
            val per = (it.first.toDouble().div(it.second) * 100).toInt()
            val done = "${it.first.toDouble().div(1024).toInt()} KB"
            val total = "${it.second.toDouble().div(1024).toInt()} KB"
            L.d(org.sourcei.wallup.deprecated.handlers.DialogHandler.NAME, "${it.first} : ${it.second} : $per")
            bar.progress = per
            percentage.text = "$per %"
            progress.text = "$done/$total"
        }, {
            did = it
        }) {
            callback(it)
            org.sourcei.wallup.deprecated.handlers.DialogHandler.dismiss()
        }

        /*did = DownloadHandler.externalDownload(url,
                path,
                "$id.jpg",
                {
                    bar.isIndeterminate = false
                    val per = (it.currentBytes.toDouble().div(it.totalBytes) * 100).toInt()
                    val done = "${it.currentBytes.toBigDecimal().div(1024.toBigDecimal()).toInt()} KB"
                    val total = "${it.totalBytes.toBigDecimal().div(1024.toBigDecimal()).toInt()} KB"
                    L.d(NAME, "${it.currentBytes} : ${it.totalBytes} : $per")
                    bar.progress = per
                    percentage.text = "$per %"
                    progress.text = "$done/$total"
                }
        ) {
            callback(it)
            dismiss()
        }*/

        org.sourcei.wallup.deprecated.handlers.DialogHandler.alertDialog.setCancelable(false)
        org.sourcei.wallup.deprecated.handlers.DialogHandler.alertDialog.show()
    }

    // dismiss
    private fun dismiss() {
        org.sourcei.wallup.deprecated.handlers.DialogHandler.alertDialog.dismiss()
    }

    // folder picker
    class FolderPicker : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val i = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            i.addCategory(Intent.CATEGORY_DEFAULT)
            i.putExtra("android.content.extra.SHOW_ADVANCED", false)
            i.putExtra("android.content.extra.FANCY", false)
            startActivityForResult(Intent.createChooser(i, "Choose directory"), 1)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            data?.let {
                Prefs.putString(org.sourcei.wallup.deprecated.utils.C.DOWNLOAD_PATH, it.data.path)
            }

            onBackPressed()
        }

        override fun onBackPressed() {
            var obj = jsonOf(Pair(org.sourcei.wallup.deprecated.utils.C.TYPE, org.sourcei.wallup.deprecated.utils.C.DOWNLOAD_PATH))
            EventBus.getDefault().post(org.sourcei.wallup.deprecated.utils.Event(obj))
            finish()
        }
    }

}