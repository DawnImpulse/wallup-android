package com.dawnimpulse.wallup.ui.sheets

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.auth.AuthGoogle
import com.dawnimpulse.wallup.ui.activities.ActivityInfo
import com.dawnimpulse.wallup.utils.reusables.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.sheet_user.*


class SheetUser : BottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var firebaseAuth: FirebaseAuth

    /**
     * on create view
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sheet_user, container, false)
    }

    /**
     * on view created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        // night mode
        sheet_user_night.setOnClickListener(this)
        sheet_user_login.setOnClickListener(this)
        sheet_user_info.setOnClickListener(this)
        sheet_user_login.setOnLongClickListener {
            if (firebaseAuth.currentUser != null)
                requireContext().openActivity(AuthGoogle::class.java) {
                    putBoolean(AUTH, false)
                }
            true
        }
    }

    /**
     * on resumed to check if user logged out / logged in
     */
    override fun onResume() {
        super.onResume()

        // checking user status
        if (firebaseAuth.currentUser != null)
            setUser()
        else
            removeUser()
    }

    /**
     * on click listener
     */
    override fun onClick(v: View?) {
        v?.let {
            when(it.id){
                sheet_user_night.id -> {
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                        Prefs.putAny(NIGHT_MODE, false)
                        F.nightMode()
                    }else{
                        Prefs.putAny(NIGHT_MODE, true)
                        F.nightMode()
                    }
                }
                sheet_user_login.id -> {
                    if (firebaseAuth.currentUser != null)
                        toast("Long press to logout")
                    else
                        requireContext().openActivity(AuthGoogle::class.java) {
                            putBoolean(AUTH, true)
                        }
                }
                sheet_user_info.id -> requireContext().openActivity(ActivityInfo::class.java)
                else -> {}
            }
        }
    }

    /**
     * set user details
     */
    @SuppressLint("SetTextI18n")
    private fun setUser() {
        sheet_user_login.setCardBackgroundColor(Colors.RED)
        sheet_user_login_text.setTextColor(Colors.WHITE)
        sheet_user_login_text.text = "LOGOUT"
        sheet_user_name.text = firebaseAuth.currentUser!!.displayName
        sheet_user_email.text = firebaseAuth.currentUser!!.email
    }

    /**
     * user is logged out
     */
    @SuppressLint("SetTextI18n")
    private fun removeUser() {
        sheet_user_login.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorAccent))
        sheet_user_login_text.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        sheet_user_login_text.text = "LOGIN"
        sheet_user_name.text = "YOUR NAME"
        sheet_user_email.text = "your@email.address"
    }
}