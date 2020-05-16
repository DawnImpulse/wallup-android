package com.dawnimpulse.wallup.auth

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dawnimpulse.wallup.utils.handlers.HandlerDialog
import com.dawnimpulse.wallup.utils.reusables.logd
import com.dawnimpulse.wallup.utils.reusables.loge
import com.dawnimpulse.wallup.utils.reusables.toast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException

class AuthGoogle : AppCompatActivity() {
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest
    private val REQ_ONE_TAP = 101

    /**
     * on create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        HandlerDialog.loading(this)
        oneTapInit()
        /*if (intent.extras!!.getBoolean(AUTH, true))
            login()
        else {
            oneTapClient.signOut()
            finish()
        }*/
    }

    /**
     * on destory
     */
    override fun onDestroy() {
        super.onDestroy()
        HandlerDialog.dismiss()
    }

    /**
     * activity result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with your backend.
                            toast("Got ID token.")
                            finish()
                        }
                        else -> {
                            // Shouldn't happen.
                            logd("No ID token or password!")
                        }
                    }
                } catch (e: ApiException) {
                    // error handling
                }
            }
        }
    }

    /**
     * init one tap login
     */
    private fun oneTapInit() {
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                //.setServerClientId(getString(R.string.google_web_client_id))
                                .setFilterByAuthorizedAccounts(true)
                                .build()
                )
                .build()
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                //.setServerClientId(getString(R.string.google_web_client_id))
                                // Show all accounts on the device.
                                .setFilterByAuthorizedAccounts(false)
                                .build())
                .build()
    }

    /**
     * one tap login
     */
    private fun login() {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this) { result ->
                    try {
                        startIntentSenderForResult(
                                result.pendingIntent.intentSender, REQ_ONE_TAP,
                                null, 0, 0, 0, null
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        // error
                        loge("some other error")
                        e.printStackTrace()
                    }
                }
                .addOnFailureListener(this) { e ->
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    // error
                    signUp()
                }
    }

    /**
     * sign up
     */
    private fun signUp() {
        oneTapClient.beginSignIn(signUpRequest)
                .addOnSuccessListener(this) { result ->
                    try {
                        startIntentSenderForResult(
                                result.pendingIntent.intentSender, REQ_ONE_TAP,
                                null, 0, 0, 0)
                    } catch (e: IntentSender.SendIntentException) {
                        loge("Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
                .addOnFailureListener(this) { e ->
                    // No Google Accounts found. Just continue presenting the signed-out UI.
                    loge(e.localizedMessage)
                }
    }

}