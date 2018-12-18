/*
ISC License

Copyright 2018, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/package com.dawnimpulse.wallup.utils

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
import androidx.core.widget.toast
import androidx.lifecycle.Lifecycle
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.handlers.DownloadHandler
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.CollectionPojo
import com.dawnimpulse.wallup.pojo.NewCollections
import com.downloader.PRDownloader
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.dialog_download.view.*
import kotlinx.android.synthetic.main.dialog_progress.view.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import me.grantland.widget.AutofitTextView
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject


/**
 * @author Saksham
 *
 * @note Last Branch Update -
 * @note Created on 2018-10-04 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 12 16 - master - download dialog
 *  Saksham - 2018 12 16 - master - progress dialog
 */
object Dialog {
    private val NAME = "Dialog"
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

        alertDialog = builder.create()
        alertDialog.show()

    }

    // new collection dialog
    // @param image - make it null if only collection to be added
    fun newCollection(context: Context, lifecycle: Lifecycle, image: String?) {
        val factory = LayoutInflater.from(context)
        val view = factory.inflate(R.layout.dialog_new_collection, null)
        alertDialog = AlertDialog.Builder(context, R.style.MyDialogTheme).create()
        alertDialog.setView(view)

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

        var obj = NewCollections("")
        var colPojo: Any = ""
        val model = UnsplashModel(lifecycle)

        // add image to new collection
        fun addImage(pojo: CollectionPojo, image: String) {
            model.addImageInCollection(image, pojo.id) { e, r ->
                e?.let {
                    L.d(NAME, e)
                    context.toast("error adding image")
                    papl.gone()
                    par.show()
                }
                r?.let {
                    pat.text = "Image Added"
                    papl.gone()
                    pad.show()

                    var json = JSONObject()
                    json.put(C.TYPE, C.IMAGE_TO_COLLECTION)
                    json.put(C.IS_ADDED, true)
                    json.put(C.COLLECTION_ID, pojo.id)
                    json.put(C.POSITION, 0)
                    json.put(C.COLLECTION, F.toJson(pojo))
                    json.put(C.IMAGE, image)
                    EventBus.getDefault().post(Event(json))

                    launch {
                        delay(500)
                        dismiss()
                    }
                }
            }
        }

        // create new collection
        fun createCollection(image: String?) {
            model.newCollection(obj) { e, r ->
                e?.let {
                    L.d(NAME, e)
                    context.toast("error creating collection")
                    pcpl.gone()
                    pal.gone()
                    pcr.show()
                }
                r?.let {
                    r as CollectionPojo
                    colPojo = r
                    pcpl.gone()
                    pcd.show()
                    pct.text = "Collection Created"

                    var obj = JSONObject()
                    obj.put(C.TYPE, C.NEW_COLLECTION)
                    obj.put(C.COLLECTION, F.toJson(r))
                    EventBus.getDefault().post(Event(obj))

                    if (image == null) {
                        launch {
                            delay(500)
                            dismiss()
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
                obj = NewCollections(title, desc, private)

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
            dismiss()
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
            addImage(colPojo as CollectionPojo, image!!)
        }


        alertDialog.show()
    }

    //download dialog
    fun download(context: Context, id: String, url: String, isWallpaper: Boolean = false) {
        val factory = LayoutInflater.from(context)
        val view = factory.inflate(R.layout.dialog_download, null)
        val shouldShow = Prefs.getBoolean(C.IMAGE_DOWNLOAD_ASK, true)
        alertDialog = AlertDialog.Builder(context, R.style.MyDialogTheme).create()
        alertDialog.setView(view)

        val path = view.downloadPath as AutofitTextView
        val or = view.downloadOT as TextView
        val orL = view.downloadO as LinearLayout
        val uhd = view.downloadUHDT as TextView
        val uhdL = view.downloadUHD as LinearLayout
        val fhd = view.downloadFHDT as TextView
        val fhdL = view.downloadFHD as LinearLayout
        val ask = view.dialogAsk as CheckBox
        val askL = view.dialogAskL as LinearLayout

        ask.setOnCheckedChangeListener { _, isChecked ->
            Prefs.putBoolean(C.IMAGE_DOWNLOAD_ASK, isChecked)
        }

        val drawable = ContextCompat.getDrawable(context, R.drawable.bt_round_complete_corners)
        val white = Colors(context).WHITE
        val black = Colors(context).BLACK

        val clickListener = View.OnClickListener {
            when (it.id) {
                view.downloadChoose.id -> {
                    context.openActivity(FolderPicker::class.java)
                    dismiss()
                }
                orL.id -> {
                    Prefs.putString(C.IMAGE_DOWNLOAD_QUALITY, C.O)
                    Config.IMAGE_DOWNLOAD_QUALITY = C.O

                    or.background = drawable
                    or.setTextColor(black)

                    uhd.background = null
                    uhd.setTextColor(white)

                    fhd.background = null
                    fhd.setTextColor(white)
                }
                uhdL.id -> {
                    Prefs.putString(C.IMAGE_DOWNLOAD_QUALITY, C.UHD)
                    Config.IMAGE_DOWNLOAD_QUALITY = C.UHD

                    or.background = null
                    or.setTextColor(white)

                    uhd.background = drawable
                    uhd.setTextColor(black)

                    fhd.background = null
                    fhd.setTextColor(white)
                }
                fhdL.id -> {
                    Prefs.putString(C.IMAGE_DOWNLOAD_QUALITY, C.FHD)
                    Config.IMAGE_DOWNLOAD_QUALITY = C.FHD

                    or.background = null
                    or.setTextColor(white)

                    uhd.background = null
                    uhd.setTextColor(white)

                    fhd.background = drawable
                    fhd.setTextColor(black)
                }
                askL.id -> {
                    val value = !ask.isChecked
                    Prefs.putBoolean(C.IMAGE_DOWNLOAD_ASK, value)
                    ask.isChecked = value
                    context.toast(Prefs.getBoolean(C.IMAGE_DOWNLOAD_ASK, true).toString())
                }
            }
        }

        if (!isWallpaper)
            path.text = Prefs.getString(C.DOWNLOAD_PATH, Config.DEFAULT_DOWNLOAD_PATH).toFileString()
        else {
            path.text = Config.DEFAULT_DOWNLOAD_PATH
            view.downloadChoose.gone()
        }

        orL.setOnClickListener(clickListener)
        uhdL.setOnClickListener(clickListener)
        fhdL.setOnClickListener(clickListener)
        askL.setOnClickListener(clickListener)
        view.downloadChoose.setOnClickListener(clickListener)

        when (Prefs.getString(C.IMAGE_DOWNLOAD_QUALITY, C.O)) {
            C.FHD -> fhdL.performClick()
            C.UHD -> uhdL.performClick()
            C.O -> orL.performClick()
        }

        fun startDownloadOrWallpaper() {
            if (isWallpaper) {
                if (path.text.toString().toFile().exists()) {
                    dismiss()
                    EventBus.getDefault().post(Event(jsonOf(Pair(C.TYPE, C.WALLPAPER))))
                } else {
                    context.toast("File path doesn't exists , please select a new one.", Toast.LENGTH_LONG)
                    Prefs.putBoolean(C.IMAGE_DOWNLOAD_ASK, true)
                }
            } else {
                if (path.text.toString().toFile().exists()) {
                    context.toast("Download starting!! Check notification for progress.")
                    val quality = Prefs.getString(C.IMAGE_DOWNLOAD_QUALITY, Config.IMAGE_DOWNLOAD_QUALITY)
                    var newUrl = url
                    if (quality != C.O)
                        newUrl = "$newUrl&q=$quality"

                    DownloadHandler.downloadData(context, newUrl, "${id}_${quality.replace("&h=", "")}",
                            Prefs.getString(C.DOWNLOAD_PATH, Config.DEFAULT_DOWNLOAD_PATH).toFileString(), isWallpaper)
                } else {
                    context.toast("File path doesn't exists , please select a new one.", Toast.LENGTH_LONG)
                    Prefs.putBoolean(C.IMAGE_DOWNLOAD_ASK, true)
                }
            }
        }

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
            startDownloadOrWallpaper()
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL") { _, _ ->
        }

        alertDialog.setCancelable(false)
        if (shouldShow) {
            alertDialog.show()
        } else {
            if (path.text.toString().toFile().exists())
                startDownloadOrWallpaper()
            else {
                context.toast("File path doesn't exists , please select a new one.", Toast.LENGTH_LONG)
                Prefs.putBoolean(C.IMAGE_DOWNLOAD_ASK, true)
                alertDialog.show()
            }
        }

    }

    //progress dialog
    fun downloadProgress(context: Context, path: String, url: String, id: String, callback: (Boolean) -> Unit) {
        var did: Int? = null
        val factory = LayoutInflater.from(context)
        val view = factory.inflate(R.layout.dialog_progress, null)
        alertDialog = AlertDialog.Builder(context, R.style.MyDialogTheme).create()
        alertDialog.setView(view)

        val progress = view.progressSize
        val percentage = view.progressPercentage
        val bar = view.progressBar as ProgressBar

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "CANCEL") { _, _ ->
            did?.let {
                PRDownloader.cancel(it)
            }
            EventBus.getDefault().post(Event(jsonOf(Pair(C.TYPE, C.CANCEL))))
            dismiss()
        }

        did = DownloadHandler.externalDownload(url,
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
        }

        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    // dismiss
    private fun dismiss() {
        alertDialog.dismiss()
    }

    // folder picker
    class FolderPicker : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val i = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            i.addCategory(Intent.CATEGORY_DEFAULT)
            i.putExtra("android.content.extra.SHOW_ADVANCED", false);
            i.putExtra("android.content.extra.FANCY", false);
            startActivityForResult(Intent.createChooser(i, "Choose directory"), 1)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            data?.let {
                L.d(NAME, it.data.path)
                L.d(NAME, it.data.toString())
                Prefs.putString(C.DOWNLOAD_PATH, it.data.path)
            }

            onBackPressed()
        }

        override fun onBackPressed() {
            var obj = jsonOf(Pair(C.TYPE, C.DOWNLOAD_PATH))
            EventBus.getDefault().post(Event(obj))
            finish()
        }
    }

}