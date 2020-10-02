package com.dawnimpulse.wallup.ui.sheets

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.utils.reusables.StyleToast
import com.dawnimpulse.wallup.utils.reusables.copy
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.sheet_id.*


class SheetId(private val id: String) : BottomSheetDialogFragment() {

    /**
     * on create view
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sheet_id, container, false)
    }

    /**
     * on view created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sheet_id_text.text = id
        sheet_id_copy.setOnClickListener {
            requireContext().copy(id)
            StyleToast.success("text copied successfully")
        }
    }

    /**
     * on dismiss
     */
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDestroy()
    }
}