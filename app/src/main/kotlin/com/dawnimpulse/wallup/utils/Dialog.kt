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
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import androidx.core.widget.toast
import androidx.lifecycle.Lifecycle
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.CollectionPojo
import com.dawnimpulse.wallup.pojo.NewCollections
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject


/**
 * @author Saksham
 *
 * @note Last Branch Update -
 * @note Created on 2018-10-04 by Saksham
 *
 * @note Updates :
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

    // dismiss
    fun dismiss() {
        alertDialog.dismiss()
    }
}